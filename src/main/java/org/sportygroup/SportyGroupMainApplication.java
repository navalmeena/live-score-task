package org.sportygroup;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SportyGroupMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(SportyGroupMainApplication.class, args);
        System.out.println("SportyGroupMainApplication Started..");
    }
}