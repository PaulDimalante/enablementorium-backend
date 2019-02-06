package com.cognizant.labs;

import com.cognizant.labs.security.EncryptionUtil;
import org.junit.Test;

import static org.junit.Assert.*;

public class ApplicationTest {

    @Test
    public void encryptionUtil() {
        EncryptionUtil encryptionUtil = new Application().encryptionUtil();
        assertNotNull(encryptionUtil);
    }
}