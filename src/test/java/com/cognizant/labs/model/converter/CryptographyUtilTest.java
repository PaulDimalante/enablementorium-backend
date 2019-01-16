package com.cognizant.labs.model.converter;

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

//    @Test
//    public void testInitializeCipher() throws Exception {
//        CryptographyUtil util = new CryptographyUtil();
//        MemberModifier.field(CryptographyUtil.class,"key").set(util,"test123456781234");
//        Cipher cipher = util.initializeCipher(Cipher.ENCRYPT_MODE);//16 byte key
//        assertNotNull(cipher);
//    }

    @Test
    public void testEncrypt() throws Exception {
        CryptographyUtil util = new CryptographyUtil();
        MemberModifier.field(CryptographyUtil.class,"key").set(util,"test123456781234");
        util.init();
        String encrypted = util.encrypt("hello");
        assertNotNull(encrypted);
        assertTrue(encrypted.length() > "hello".length());
    }

    @Test
    public void testDecrypt() throws Exception {
        CryptographyUtil util = new CryptographyUtil();
        MemberModifier.field(CryptographyUtil.class,"key").set(util,"test123456781234");
        util.init();
        String encrypted = util.encrypt("hello");
        assertNotNull(encrypted);
        assertTrue(encrypted.length() > "hello".length());
        //now decrypt it
        String result = util.decrypt(encrypted);
        assertEquals(result,"hello");
    }
}