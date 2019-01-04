package com.cognizant.labs;

import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.assertNotNull;

public class ClasspathTest {

    @Test
    public void test() throws Exception {
        assertNotNull(ClassLoader.getSystemResource("bootstrap.yml"));
    }
}
