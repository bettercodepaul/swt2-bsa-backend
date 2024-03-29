package de.bogenliga.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * I´m the starting point for the spring boot application
 */
@SpringBootApplication
@EnableScheduling
public class BogenligaApplication {

    /**
     * main method to start the spring boot application
     *
     * @param args command ling arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(BogenligaApplication.class, args);
    }

    
}
