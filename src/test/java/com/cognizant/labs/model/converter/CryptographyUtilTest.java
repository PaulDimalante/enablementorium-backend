package com.cognizant.labs.model.converter;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.crypto.Cipher;

import static org.junit.Assert.*;

//@Ignore
@PowerMockIgnore("javax.crypto.*")
@RunWith(PowerMockRunner.class)
public class CryptographyUtilTest {

    CryptographyUtil util = new CryptographyUtil();

    @Before
    public void before() throws Exception {
        MemberModifier.field(CryptographyUtil.class,"key").set(util,"test123456781234");
        MemberModifier.field(CryptographyUtil.class,"iv").set(util,"87b7225d16ea2ae1f41d0b13fdce9bba");
    }

    @Test
    public void testEncrypt() throws Exception {
        String encrypted = util.encrypt("hello");
        assertNotNull(encrypted);
        assertTrue(encrypted.length() > "hello".length());
    }

    @Test
    public void testEncryptLoop() throws Exception {
        for (int i=0;i<10;i++) {
            System.out.println(util.encrypt("hello"));
        }
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