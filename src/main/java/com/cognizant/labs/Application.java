package com.cognizant.labs;

import com.cognizant.labs.security.EncryptionUtil;
import com.cognizant.labs.security.KeyCloakOAuthPermissionEvaluator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@SpringBootApplication
public class Application {

    public static void main(String... args) {
        SpringApplication.run(Application.class,args);
    }

    @Value("${encryption.key}")
    private String key;

    @Value("${encryption.iv}")
    private String iv;

    @Value("${encryption.keystore.password}")
    private String keyStorePassword;

    @Value("${encryption.certificate.password}")
    private String certificatePassword;

    @Value("${encryption.certificate.name}")
    private String certificateName;

    @Bean
    public EncryptionUtil encryptionUtil() {
        EncryptionUtil encryptionUtil = new EncryptionUtil();
        EncryptionUtil.setCertificateName(certificateName);
        EncryptionUtil.setCertificatePassword(certificatePassword);
        EncryptionUtil.setKeyStorePassword(keyStorePassword);
        //return
        return encryptionUtil;
    }

    @Profile("local")
    @Configuration
    static class DisableSecurity extends WebSecurityConfigurerAdapter {

        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring().antMatchers("/**");
        }
    }

    @Profile("default")
    @Configuration
    class SecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                    .oauth2ResourceServer().jwt();
        }
    }

    @Profile("default")
    @Bean
    public PermissionEvaluator permissionEvaluator() {
        return new KeyCloakOAuthPermissionEvaluator();
    }

    @Profile("default")
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    @Configuration
    class EvaluatorConfig extends GlobalMethodSecurityConfiguration {

        @Override
        protected MethodSecurityExpressionHandler createExpressionHandler() {
            DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
            expressionHandler.setPermissionEvaluator(permissionEvaluator());
            return expressionHandler;
        }
    }
}
