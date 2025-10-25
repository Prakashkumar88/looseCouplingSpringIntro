package service.impl;

import domain.Account;
import domain.Customer;
import domain.Transaction;
import domain.Type;
import repository.AccountRepository;
import repository.CustomerRepository;
import repository.TransactionRepository;
import service.BankService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class BankServiceImpl implements BankService {
    private final AccountRepository accountRepository = new AccountRepository();
    private final TransactionRepository transactionRepository = new TransactionRepository();
    private final CustomerRepository customerRepository = new CustomerRepository();

    @Override
    public String openAccount(String name, String email, String accountType){
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
        Account account = accountRepository.findByNumber(accountNumber).orElseThrow(() -> new RuntimeException("Account Not Found" + accountNumber));

        account.setBalance(account.getBalance() + amount);
        Transaction transaction = new Transaction(account.getAccountNumber(), amount, UUID.randomUUID().toString(), note, LocalDateTime.now(), Type.DEPOSIT);
        transactionRepository.add(transaction);
    }

    @Override
    public void withDraw(String accountNumber, Double amount, String note) {
        Account account = accountRepository.findByNumber(accountNumber).orElseThrow(() -> new RuntimeException("Account Not Found" + accountNumber));

        if(account.getBalance() < amount) throw new RuntimeException("Insufficient Balance");
        account.setBalance(account.getBalance() - amount);
        Transaction transaction = new Transaction(account.getAccountNumber(), amount, UUID.randomUUID().toString(), note, LocalDateTime.now(), Type.WITHDRAW);
        transactionRepository.add(transaction);
    }

    @Override
    public void transfer(String from, String to, Double amount, String note) {
        if(from.equals(to)) throw new RuntimeException("Cannot transfer to your own account");
        Account fromAccount = accountRepository.findByNumber(from).orElseThrow(() -> new RuntimeException("Account Not Found" + from));
        Account toAccount = accountRepository.findByNumber(to).orElseThrow(() -> new RuntimeException("Account Not Found" + to));

        if(fromAccount.getBalance() < amount) throw new RuntimeException("Insufficient Balance");
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
