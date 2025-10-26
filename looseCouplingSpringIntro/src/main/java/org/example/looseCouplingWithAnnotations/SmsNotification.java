package org.example.looseCouplingWithAnnotations;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component("UserServiceEmail")// Registers this class as a Spring bean with a specific name to distinguish it from other UserService implementations
@Primary
public class SmsNotification implements NotificationService {
    @Override
    public void send(String message) {
        System.out.println("SMS: " + message);
    }
}
