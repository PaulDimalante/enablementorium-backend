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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Random;

@Component("crypto")
public class CryptographyUtil {

    private static final Log logger = LogFactory.getLog(CryptographyUtil.class);

    private static final String CIPHER_INSTANCE_NAME = "AES/CBC/PKCS5PADDING";
    private static final String SECRET_KEY_ALGORITHM = "AES";
    private static final String PROVIDER = "BC";

    private static final Charset UTF8 = Charset.forName("UTF-8");
    private static final int AES_NIVBITS = 128;
    private KeyParameter aesKey;

    private static byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

    @Value("${encryption.key}")
    private String key;

    @Value("${encryption.salt}")
    private String salt;

    private Cipher cipher;


    public String encrypt(String attribute) {

        IvParameterSpec ivspec = new IvParameterSpec(iv);

        SecretKeyFactory factory = null;
        Cipher cipher = null;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(key.toCharArray(), salt.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(attribute.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | UnsupportedEncodingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            logger.error(e);
        }
        return null;
    }

    public String decrypt(String dbData) {
        byte[] encryptedBytes = Base64.getDecoder().decode(dbData);
        byte[] decryptedBytes = new byte[0];
        try {
            // Extract the IV, which is stored in the next N bytes
            int nIvBytes = AES_NIVBITS / 8;
            byte[] ivBytes = new byte[nIvBytes];
            System.arraycopy(encryptedBytes, 0, ivBytes, 0, nIvBytes);

            // Select encryption algorithm and padding : AES with CBC and PCKS#7.
            // Note that the "encryption strength" (128 or 256 bit key) is set by the KeyParameter object.
            KeyParameter keyParam = getAesKey();
            CipherParameters params = new ParametersWithIV(keyParam, ivBytes);
            BlockCipherPadding padding = new PKCS7Padding();
            BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESEngine()), padding);

            // Decrypt all bytes that follow the IV
            cipher.reset();
            cipher.init(false, params); // first param = encode/decode

            byte[] bytesDec = new byte[1];

            try {
                int buflen = cipher.getOutputSize(encryptedBytes.length - nIvBytes);
                byte[] workingBuffer = new byte[buflen];
                int len = cipher.processBytes(encryptedBytes, nIvBytes, encryptedBytes.length - nIvBytes, workingBuffer, 0);
                len += cipher.doFinal(workingBuffer, len);

                // Note that getOutputSize returns a number which includes space for "padding" bytes to be stored in.
                // However we don't want these padding bytes; the "len" variable contains the length of the *real* data
                // (which is always less than the return value of getOutputSize.
                bytesDec = new byte[len];
                System.arraycopy(workingBuffer, 0, bytesDec, 0, len);
            } catch (InvalidCipherTextException e) {
                logger.error(e);
            } catch (RuntimeException e) {
                logger.error(e);
            }

            // And convert the result to a string
            return new String(bytesDec, UTF8);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            logger.error(e);
        } catch (GeneralSecurityException e) {
            logger.error(e);
        }
        return new String(decryptedBytes);
    }


    private KeyParameter getAesKey() throws GeneralSecurityException {
        if (this.aesKey != null) {
            return this.aesKey;
        }
        byte[] rawKeyData = key.getBytes(); // somehow obtain the raw bytes of the key
        // Wrap the key data in an appropriate holder type
        this.aesKey = new KeyParameter(rawKeyData);
        return this.aesKey;
    }
}
