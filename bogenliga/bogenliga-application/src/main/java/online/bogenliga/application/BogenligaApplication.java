package online.bogenliga.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * I´m the starting point for the spring boot application
 */
@SpringBootApplication
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
