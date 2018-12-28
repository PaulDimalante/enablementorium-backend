package com.cognizant.labs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@EnableResourceServer
@SpringBootApplication
public class Application {

    public static void main(String... args) throws Exception {
        SpringApplication.run(Application.class,args);
    }
}
