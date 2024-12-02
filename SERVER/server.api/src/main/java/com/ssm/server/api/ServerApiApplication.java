package com.ssm.server.api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerApiApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ServerApiApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception { 
    }

    public static Boolean debug(){
        return true; 
    }
}
