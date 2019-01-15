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
@ActiveProfiles({"default","test"})
public class ApplicationIT {

    @Autowired
    ApplicationContext context;

    @Test
    public void test() throws Exception {
        assertTrue(context.getBeanDefinitionCount() > 0);
    }

    @Test
    public void testBeanIsCorrect() throws Exception {
        assertNotNull(context.getBean(Application.SecurityConfig.class));
    }

    @Test(expected = NoSuchBeanDefinitionException.class)
    public void testBeanIsNotPresent() throws Exception {
        context.getBean(Application.DisabledSecurity.class);
    }

}