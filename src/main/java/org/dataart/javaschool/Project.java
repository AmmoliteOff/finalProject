package org.dataart.javaschool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PreDestroy;

@PropertySource("classpath:application.properties")
@SpringBootApplication
public class Project  {
    public static void main(String[] args)  {
        SpringApplication.run(Project.class, args);
    }
}