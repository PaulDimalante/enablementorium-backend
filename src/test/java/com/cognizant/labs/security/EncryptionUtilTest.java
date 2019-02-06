package com.cognizant.labs.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;

@PowerMockIgnore("javax.crypto.*")
@RunWith(PowerMockRunner.class)
public class EncryptionUtilTest {

    EncryptionUtil util;

    @Before
    public void before() throws Exception {
        util = new EncryptionUtil();
        MemberModifier.field(EncryptionUtil.class,"key").set(util,"test123456781234");
        MemberModifier.field(EncryptionUtil.class,"iv").set(util,"87b7225d16ea2ae1f41d0b13fdce9bba");
        MemberModifier.field(EncryptionUtil.class,"keyStorePassword").set(util,"Ilove2cod#");
        MemberModifier.field(EncryptionUtil.class,"certificatePassword").set(util,"Ilove2cod#");
        MemberModifier.field(EncryptionUtil.class,"certificateName").set(util,"mykey");
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

    @Test(expected = AssertionError.class)
    public void testDecryptException() throws Exception {
        String encrypted = util.encrypt("hello");
        assertNotNull(encrypted);
        assertTrue(encrypted.length() > "hello".length());
        //now decrypt it
        String result = util.decrypt("hello");
        assertEquals(result,"hello");
    }

    @Test
    public void testEncryptCertificate() {
        String value = "hello";
        String result = util.encryptCertificate(value);
        assertNotNull(result);
        assertTrue(result.length() > value.length());
    }

    @Test
    public void testDecryptCertificate() {
        String value = "hello";
        String result = util.encryptCertificate(value);
        assertNotNull(result);
        assertTrue(result.length() > value.length());
        //now decrypt
        String decrypted = util.decryptCertificate(result);
        assertNotNull(decrypted);
        assertEquals(decrypted,value);
    }

    @Test
    public void testNullEncrypt() throws IllegalAccessException {
        //change the values
        MemberModifier.field(EncryptionUtil.class,"keyStorePassword").set(util,"blah");
        MemberModifier.field(EncryptionUtil.class,"certificatePassword").set(util,"blah");
        MemberModifier.field(EncryptionUtil.class,"certificateName").set(util,"blah");
        //do it
        assertNull(util.encryptCertificate("blah"));
    }

    @Test
    public void testNullDecrypt() throws IllegalAccessException {
        //change the values
        MemberModifier.field(EncryptionUtil.class,"keyStorePassword").set(util,"blah");
        MemberModifier.field(EncryptionUtil.class,"certificatePassword").set(util,"blah");
        MemberModifier.field(EncryptionUtil.class,"certificateName").set(util,"blah");
        //do it
        assertNull(util.decryptCertificate("blah"));
    }
}