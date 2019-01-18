package com.cognizant.labs.model.converter;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.BlockCipherPadding;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Random;

@Component("crypto")
public class CryptographyUtil {
    private static final Log logger = LogFactory.getLog(CryptographyUtil.class);

    private static final String CIPHER_INSTANCE_NAME = "AES/CBC/PKCS5Padding";
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

    private Cipher cipher;

    protected Cipher getCipher(int encryptionMode) {
        try {
            cipher = Cipher.getInstance(CIPHER_INSTANCE_NAME, PROVIDER);
            SecretKey secretKey = new SecretKeySpec(key.getBytes(), SECRET_KEY_ALGORITHM);
            AlgorithmParameterSpec algorithmParameters = getAlgorithmParameterSpec(cipher);

            cipher.init(encryptionMode, secretKey,algorithmParameters);
        } catch (InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException e) {
            logger.error(e);
        }
        return cipher;
    }

    public String encrypt(String attribute) {
        byte[] bytesToEncrypt = attribute.getBytes();
        byte[] encryptedBytes = new byte[0];
        try {
            encryptedBytes = callCipherDoFinal(getCipher(Cipher.ENCRYPT_MODE), bytesToEncrypt);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            logger.error(e);
        }
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String dbData) {
        byte[] encryptedBytes = Base64.getDecoder().decode(dbData);
        byte[] decryptedBytes = new byte[0];
        try {
            decryptedBytes = callCipherDoFinal(getCipher(Cipher.DECRYPT_MODE), encryptedBytes);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            logger.error(e);
        }
        return new String(decryptedBytes);
    }

    private byte[] callCipherDoFinal(Cipher cipher, byte[] bytes) throws IllegalBlockSizeException, BadPaddingException {
        return cipher.doFinal(bytes);
    }

    int getCipherBlockSize(Cipher cipher) {
        return cipher.getBlockSize();
    }

    private AlgorithmParameterSpec getAlgorithmParameterSpec(Cipher cipher) {
        byte[] iv = new byte[getCipherBlockSize(cipher)];
        return new IvParameterSpec(iv);
    }

}
