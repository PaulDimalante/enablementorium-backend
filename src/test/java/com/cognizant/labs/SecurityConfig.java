package com.cognizant.labs;

import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Profile("test")
public class SecurityConfig extends ResourceServerConfigurerAdapter {
}
