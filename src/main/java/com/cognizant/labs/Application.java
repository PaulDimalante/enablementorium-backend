package com.cognizant.labs;

import com.cognizant.labs.security.EncryptionUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
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

    @Profile("test")
    @Configuration
    static class DisableSecurity extends WebSecurityConfigurerAdapter {

        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring().antMatchers("/**");
        }
    }

    @Profile("default")
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    @Configuration
    static class SecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                    .oauth2ResourceServer().jwt();
        }
    }
}
