package org.example.loose;

public class UserService {
    private NotificationService notificationService;

    // 🟢 Default constructor required for property injection
    public UserService() {
    }

    public UserService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void notifyUser(String message) {
        notificationService.send(message);
    }
}
