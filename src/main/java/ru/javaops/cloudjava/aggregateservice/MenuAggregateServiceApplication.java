package ru.javaops.cloudjava.aggregateservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MenuAggregateServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MenuAggregateServiceApplication.class, args);
    }
}
