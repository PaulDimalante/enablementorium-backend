package com.cognizant.labs;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles({"local","test"})
public class ApplicationLocalProfileIT {

    @Autowired
    ApplicationContext context;

    @Test
    public void testBeanIsCorrect() throws Exception {
        assertNotNull(context.getBean(Application.DisabledSecurity.class));
    }

    @Test(expected = NoSuchBeanDefinitionException.class)
    public void testBeanIsNotPresent() throws Exception {
        assertNull(context.getBean(Application.SecurityConfig.class));
    }

}