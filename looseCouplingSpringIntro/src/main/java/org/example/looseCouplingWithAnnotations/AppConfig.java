package org.example.looseCouplingWithAnnotations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "org.example")
public class AppConfig {

    @Bean(initMethod = "init", destroyMethod = "cleanup") // Used when working with legacy or external classes to run custom init and cleanup methods

    public LifeCycleBean lifeCycleBean(NotificationService notificationService){
        return new LifeCycleBean(notificationService);
    }
}
