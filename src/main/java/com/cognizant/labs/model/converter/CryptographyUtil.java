package com.cognizant.labs.model.converter;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Base64;

@Component
public class CryptographyUtil {

    private static final String CIPHER_INSTANCE_NAME = "AES/ECB/PKCS5Padding";
    private static final String SECRET_KEY_ALGORITHM = "AES";
    private static final String PROVIDER = "BC";

    @Value("${encryption.key}")
    private String key;

    static {
        boolean hasBC = false;
        for (Provider provider : Security.getProviders()) {
            if (provider.getName().equals(new BouncyCastleProvider().getName())) {
                hasBC = true;
                break;
            }//end if
        }//end for
        if (!hasBC) {
            Security.addProvider(new BouncyCastleProvider());
        }//end if
    }

    private Cipher encryptCipher;

    private Cipher decryptCipher;

    @PostConstruct
    public void init() {
        Assert.notNull(key,"No Key has been set");
        this.encryptCipher = initializeCipher(Cipher.ENCRYPT_MODE);
        this.decryptCipher = initializeCipher(Cipher.DECRYPT_MODE);
    }

    protected Cipher initializeCipher(int encryptionMode) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(CIPHER_INSTANCE_NAME, PROVIDER);
            SecretKey secretKey = new SecretKeySpec(key.getBytes(), SECRET_KEY_ALGORITHM);
            cipher.init(encryptionMode, secretKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return cipher;
    }

    public String encrypt(String attribute) {
        byte[] bytesToEncrypt = attribute.getBytes();
        byte[] encryptedBytes = new byte[0];
        try {
            encryptedBytes = callCipherDoFinal(encryptCipher, bytesToEncrypt);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String dbData) {
        byte[] encryptedBytes = Base64.getDecoder().decode(dbData);
        byte[] decryptedBytes = new byte[0];
        try {
            decryptedBytes = callCipherDoFinal(decryptCipher, encryptedBytes);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return new String(decryptedBytes);
    }

    private byte[] callCipherDoFinal(Cipher cipher, byte[] bytes) throws IllegalBlockSizeException, BadPaddingException {
        return cipher.doFinal(bytes);
    }
}
