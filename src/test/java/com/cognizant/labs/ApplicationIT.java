package com.cognizant.labs;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationIT {

    @Autowired
    ApplicationContext context;

    @Test
    public void test() throws Exception {
        assertTrue(context.getBeanDefinitionCount() > 0);
    }

    @Test
    public void testEncryptionBean() {
        assertNotNull(new Application().encryptionUtil());
    }
}