package com.cognizant.labs.model.converter;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Base64;

@Component
public class CryptographyUtil {

    private static final String CIPHER_INSTANCE_NAME = "AES/ECB/PKCS5Padding";
    private static final String SECRET_KEY_ALGORITHM = "AES";
    private static final String PROVIDER = "BC";

    @Value("${encryption.key}")
    private String key;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public Cipher initializeCipher(int encryptionMode) {
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

    public String encrypt(Cipher cipher, String attribute) {
        byte[] bytesToEncrypt = attribute.getBytes();
        byte[] encryptedBytes = new byte[0];
        try {
            encryptedBytes = callCipherDoFinal(cipher, bytesToEncrypt);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(Cipher cipher, String dbData) {
        byte[] encryptedBytes = Base64.getDecoder().decode(dbData);
        byte[] decryptedBytes = new byte[0];
        try {
            decryptedBytes = callCipherDoFinal(cipher, encryptedBytes);
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
