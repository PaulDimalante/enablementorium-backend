package com.cognizant.labs.model.converter;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.crypto.Cipher;

import static org.junit.Assert.*;

@Ignore
@RunWith(PowerMockRunner.class)
public class CryptographyUtilTest {

    CryptographyUtil util = new CryptographyUtil();

    @Before
    public void before() throws Exception {
        MemberModifier.field(CryptographyUtil.class,"key").set(util,"test123456781234");
    }

    @Test
    public void testEncrypt() throws Exception {
        String encrypted = util.encrypt("hello");
        assertNotNull(encrypted);
        assertTrue(encrypted.length() > "hello".length());
    }

    @Test
    public void testDecrypt() throws Exception {
        String encrypted = util.encrypt("hello");
        assertNotNull(encrypted);
        assertTrue(encrypted.length() > "hello".length());
        //now decrypt it
        String result = util.decrypt(encrypted);
        assertEquals(result,"hello");
    }
}