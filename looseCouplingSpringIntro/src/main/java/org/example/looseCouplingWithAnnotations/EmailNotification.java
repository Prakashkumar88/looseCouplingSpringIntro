package org.example.looseCouplingWithAnnotations;

import org.springframework.stereotype.Component;

@Component
public class EmailNotification implements NotificationService {
    @Override
    public void send(String message) {
        System.out.println("Email: " + message);
    }
}
