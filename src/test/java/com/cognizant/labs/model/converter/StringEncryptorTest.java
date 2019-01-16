package com.cognizant.labs.model.converter;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Ignore
@RunWith(PowerMockRunner.class)
public class StringEncryptorTest {

    @Test
    public void convertToDatabaseColumn() throws Exception {
        CryptographyUtil util = new CryptographyUtil();
        MemberModifier.field(CryptographyUtil.class,"key").set(util,"test123456781234");
        util.init();
        StringEncryptor stringEncryptor = new StringEncryptor(util);
        String result = stringEncryptor.convertToDatabaseColumn("hello");
        assertNotNull(result);
        assertTrue(result.length() > "hello".length());
    }

}