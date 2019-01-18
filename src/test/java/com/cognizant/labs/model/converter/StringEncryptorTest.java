package com.cognizant.labs.model.converter;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;

//@Ignore
@RunWith(PowerMockRunner.class)
public class StringEncryptorTest {

    private StringEncryptor stringEncryptor;

    private CryptographyUtil util;

    @Before
    public void before() throws Exception {
        util = new CryptographyUtil();
        MemberModifier.field(CryptographyUtil.class,"key").set(util,"test123456781234");
        stringEncryptor = new StringEncryptor();
        MemberModifier.field(StringEncryptor.class,"cryptographyUtil").set(stringEncryptor,util);
    }

    @Test
    public void testConvertToDatabaseColumn() throws Exception {
        String result = stringEncryptor.convertToDatabaseColumn("hello");
        assertNotNull(result);
        assertTrue(result.length() > "hello".length());
    }

    @Test
    public void testConvertToEntityAttribute() throws Exception {
        //encrypt a value
        String encrypted = util.encrypt("hello");
        //now process
        String result = stringEncryptor.convertToEntityAttribute(encrypted);
        assertNotNull(result);
        assertTrue(result.equals("hello"));
    }

    @Test
    public void testNullConvertToEntityAttribute() throws Exception {
        //now process
        String result = stringEncryptor.convertToEntityAttribute(null);
        assertNull(result);
    }

    @Test
    public void testNullConvertToDatabaseColumn() throws Exception {
        //now process
        String result = stringEncryptor.convertToDatabaseColumn(null);
        assertNull(result);
    }
}