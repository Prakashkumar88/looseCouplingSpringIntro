package service.impl;

import domain.Account;
import domain.Customer;
import domain.Transaction;
import domain.Type;
import exception.AccountNotFoundException;
import exception.InsufficientFundsException;
import exception.ValidationException;
import repository.AccountRepository;
import repository.CustomerRepository;
import repository.TransactionRepository;
import service.BankService;
import util.Validation;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class BankServiceImpl implements BankService {
    private final AccountRepository accountRepository = new AccountRepository();
    private final TransactionRepository transactionRepository = new TransactionRepository();
    private final CustomerRepository customerRepository = new CustomerRepository();
    private final Validation<String> validateName = name -> {
        if(name == null || name.isBlank()) throw new ValidationException("Name is required");
    };
    private final Validation<String> validateEmail = email -> {
        if(email == null || !email.contains("@")) throw new ValidationException("Valid email is required");
    };
    private final Validation<String> validateType = type -> {
        if (type == null || !(type.equalsIgnoreCase("SAVINGS") || type.contains("CURRENT")))
            throw new ValidationException("Type must be SAVINGS or CURRENT");
    };
    private final Validation<Double> validateAmountPositive = amount -> {
        if (amount == null || amount < 0)
            throw new ValidationException("Please enter valid amount");
    };


    @Override
    public String openAccount(String name, String email, String accountType){
        validateName.validate(name);
        validateEmail.validate(email);
        validateType.validate(accountType);

        String customerId = UUID.randomUUID().toString();
//        String accountNumber = UUID.randomUUID().toString();
        Customer customer = new Customer(customerId,name, email);
        customerRepository.save(customer);

        String accountNumber = getAccountNumber();

        Account account = new Account(accountNumber, customerId, 0, accountType);
        accountRepository.save(account);

        return accountNumber;
    }

    @Override
    public List<Account> listAccounts() {
        return accountRepository.findAll().stream().sorted(Comparator.comparing(Account::getAccountNumber)).collect(Collectors.toList());
    }

    @Override
    public void deposit(String accountNumber, Double amount, String note) {
        validateAmountPositive.validate(amount);
        Account account = accountRepository.findByNumber(accountNumber).orElseThrow(() -> new AccountNotFoundException("Account Not Found" + accountNumber));

        account.setBalance(account.getBalance() + amount);
        Transaction transaction = new Transaction(account.getAccountNumber(), amount, UUID.randomUUID().toString(), note, LocalDateTime.now(), Type.DEPOSIT);
        transactionRepository.add(transaction);
    }

    @Override
    public void withDraw(String accountNumber, Double amount, String note) {
        validateAmountPositive.validate(amount);
        Account account = accountRepository.findByNumber(accountNumber).orElseThrow(() -> new AccountNotFoundException("Account Not Found" + accountNumber));

        if(account.getBalance() < amount) throw new InsufficientFundsException("Insufficient Balance");
        account.setBalance(account.getBalance() - amount);
        Transaction transaction = new Transaction(account.getAccountNumber(), amount, UUID.randomUUID().toString(), note, LocalDateTime.now(), Type.WITHDRAW);
        transactionRepository.add(transaction);
    }

    @Override
    public void transfer(String from, String to, Double amount, String note) {
        validateAmountPositive.validate(amount);
        if(from.equals(to)) throw new ValidationException("Cannot transfer to your own account");
        Account fromAccount = accountRepository.findByNumber(from).orElseThrow(() -> new AccountNotFoundException("Account Not Found: " + from));
        Account toAccount = accountRepository.findByNumber(to).orElseThrow(() -> new AccountNotFoundException("Account Not Found: " + to));

        if(fromAccount.getBalance() < amount) throw new InsufficientFundsException("Insufficient Balance");
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        transactionRepository.add(new Transaction(fromAccount.getAccountNumber(), amount, UUID.randomUUID().toString(), note, LocalDateTime.now(), Type.TRANSFER_OUT));
        transactionRepository.add(new Transaction(toAccount.getAccountNumber(), amount, UUID.randomUUID().toString(), note, LocalDateTime.now(), Type.TRANSFER_IN));
    }

    @Override
    public List<Transaction> getStatement(String accountNumber) {
        return transactionRepository.findByAccountNumber(accountNumber).stream().sorted(Comparator.comparing(Transaction::getTimeStamp)).collect(Collectors.toList());
    }

    @Override
    public List<Account> searchAccountsByCustomerName(String q) {
        String query = q == null ? "" : q.toLowerCase();
//        List<Account> result = new ArrayList<>();
//        for(Customer c: customerRepository.findAll()){
//            if(c.getName().toLowerCase().contains(query)) result.addAll(accountRepository.findByCustomerId(c.getId()));
//
//        }
//        result.sort(Comparator.comparing(Account::getAccountNumber));
        return customerRepository.findAll().stream()
                .filter(c -> c.getName().toLowerCase().contains(query))
                .flatMap(c -> accountRepository.findByCustomerId(c.getId()).stream())
                .sorted(Comparator.comparing(Account::getAccountNumber))
                .collect(Collectors.toList());

//        return result;
    }

    private String getAccountNumber() {
        int size = accountRepository.findAll().size() + 1;
        return String.format("AC%06d", size);
    }

}
