package org.example;

import org.example.looseCouplingWithAnnotations.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Spring Application Context");
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        System.out.println("Retrieving LifeCycle Bean");
        LifeCycleBean lifeCycleBean = context.getBean(LifeCycleBean.class);
        lifeCycleBean.performTask();

        System.out.println("Closing Spring Context");
    }
}
