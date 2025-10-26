package org.example.looseCouplingWithAnnotations;

import org.springframework.stereotype.Component;

//@Component if you want the spring to handle the objects
public class LifeCycleBean {
    private NotificationService notificationService;

    public LifeCycleBean(NotificationService notificationService){
        System.out.println("Constructor called: Deependenc Injected");
        this.notificationService = notificationService;
    }

    public void init(){
        System.out.println("init called: Bean initialized");
        notificationService.send("Hello from init()");
    }

    public void performTask(){
        System.out.println("Ready for the use");
    }

    public void cleanup(){
        System.out.println("Cleanup()  being called ");
    }
}
