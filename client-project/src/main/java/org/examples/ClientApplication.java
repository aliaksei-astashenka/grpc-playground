package org.examples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        java.security.Security.setProperty("networkaddress.cache.ttl" , "0");
        SpringApplication.run(ClientApplication.class, args);
    }
}
