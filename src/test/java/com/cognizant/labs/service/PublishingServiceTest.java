package com.cognizant.labs.service;

import com.cognizant.labs.model.Person;
import com.cognizant.labs.security.EncryptionUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

@PowerMockIgnore("javax.crypto.*")
@RunWith(PowerMockRunner.class)
public class PublishingServiceTest {

    @Test
    public void testSend() throws Exception {
        PublishingService service = new PublishingService();
        //mocks
        PublicKey publicKey = mock(PublicKey.class);
        PrivateKey privateKey = mock(PrivateKey.class);
        KeyPair keyPair = new KeyPair(publicKey,privateKey);
        EncryptionUtil encryptionUtil = new EncryptionUtil();
        MemberModifier.field(EncryptionUtil.class,"key").set(encryptionUtil,"test123456781234");
        MemberModifier.field(EncryptionUtil.class,"iv").set(encryptionUtil,"87b7225d16ea2ae1f41d0b13fdce9bba");
        MemberModifier.field(EncryptionUtil.class,"keyStorePassword").set(encryptionUtil,"Ilove2cod#");
        MemberModifier.field(EncryptionUtil.class,"certificatePassword").set(encryptionUtil,"Ilove2cod#");
        MemberModifier.field(EncryptionUtil.class,"certificateName").set(encryptionUtil,"mykey");
        //add the crypto
        MemberModifier.field(PublishingService.class,"encryptionUtil").set(service,encryptionUtil);
        //execute
        assertNull(service.getPerson());
        service.sendUpdate(new Person());
        assertNotNull(service.getPerson());
    }

}