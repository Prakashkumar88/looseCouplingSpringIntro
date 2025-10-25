package org.example;

import org.example.loose.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationBeanContext.xml");

        GreetingServiceDemo greetingServiceDemo = (GreetingServiceDemo) context.getBean("myBean");
        greetingServiceDemo.sayHello();

        UserService userService = (UserService) context.getBean("UserServiceSMS");
        userService.notifyUser("What's up!");

        UserService userServiceEmail = (UserService) context.getBean("UserServiceEmail");
        userServiceEmail.notifyUser("Welcome Prakash!");
    }
}
