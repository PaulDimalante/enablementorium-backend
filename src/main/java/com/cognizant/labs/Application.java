package com.cognizant.labs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
public class Application {

    public static void main(String... args) throws Exception {
        SpringApplication.run(Application.class,args);
    }

    @Profile("local")
    @Configuration
    class DisabledSecurity extends WebSecurityConfigurerAdapter {
        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring().antMatchers("/**");
        }
    }

    @Profile("default")
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    @EnableResourceServer
    @Configuration
    class SecurityConfig { }
}
