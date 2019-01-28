package com.cognizant.labs.security;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.jxpath.ri.model.beans.NullPointer;
import org.apache.commons.lang.ArrayUtils;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class EncryptionUtil {

    private static final Log logger = LogFactory.getLog(EncryptionUtil.class);

    @Value("${encryption.key}")
    private String key;

    @Value("${encryption.iv}")
    private String iv;

    private static String keyStorePassword;

    private static String certificatePassword;

    private static String certificateName;

    private static String keyStoreLocation = "/keystore.jks";

    private static final String KEYSTORE_TYPE = "PKCS12";

    public String encrypt(String attribute)  {
        final byte [] plaintext = attribute.getBytes();
        final GCMBlockCipher gcm = new GCMBlockCipher(new AESEngine());
        final CipherParameters ivAndKey;
        try {
            ivAndKey = new ParametersWithIV(new KeyParameter(key.getBytes()), iv.getBytes());
            gcm.init(true, ivAndKey);
            final byte [] inputBuf = new byte[gcm.getOutputSize(plaintext.length)];
            final int length1 = gcm.processBytes(plaintext, 0, plaintext.length, inputBuf, 0);
            final int length2 = gcm.doFinal(inputBuf, length1);
            final byte [] info_ciphertext = ArrayUtils.subarray(inputBuf, 0, length1 + length2);
            return new String(Hex.encodeHex(info_ciphertext));
        } catch (InvalidCipherTextException e) {
            logger.error(e);
            return null;
        }
    }

    public String decrypt(String dbData) {
        try {
            final byte [] ciphertext = Hex.decodeHex(dbData.toCharArray());
            final GCMBlockCipher gcm = new GCMBlockCipher(new AESEngine());
            final CipherParameters ivAndKey = new ParametersWithIV(new KeyParameter(key.getBytes()), iv.getBytes());
            gcm.init(false, ivAndKey);
            final int minSize = gcm.getOutputSize(ciphertext.length);
            final byte [] outBuf = new byte[minSize];
            final int length1 = gcm.processBytes(ciphertext, 0, ciphertext.length, outBuf, 0);

            final int length2 = gcm.doFinal(outBuf, length1);
            return new String(outBuf, 0, length1 + length2, Charset.forName("UTF-8"));
        } catch (InvalidCipherTextException | DecoderException e) {
            logger.error(e);
            return null;
        }
    }


    public String encryptCertificate(String data) {
        try {
            //get the keypair
            KeyPair keyPair = getKeyPairFromKeyStore();
            Cipher encryptCipher = Cipher.getInstance("RSA");
            encryptCipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());

            byte[] cipherText = encryptCipher.doFinal(data.getBytes(UTF_8));

            return Base64.getEncoder().encodeToString(cipherText);
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }

    public String decryptCertificate(String data) {
        try {
            KeyPair keyPair = getKeyPairFromKeyStore();
            byte[] bytes = Base64.getDecoder().decode(data);

            Cipher decriptCipher = Cipher.getInstance("RSA");
            decriptCipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());

            return new String(decriptCipher.doFinal(bytes), UTF_8);
        } catch (NullPointerException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            logger.error(e);
        }
        return null;
    }


    public static KeyPair getKeyPairFromKeyStore() {
        try {
            InputStream ins = EncryptionUtil.class.getResourceAsStream(keyStoreLocation);
            KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
            keyStore.load(ins, keyStorePassword.toCharArray());   //Keystore password
            KeyStore.PasswordProtection keyPassword =       //Key password
                    new KeyStore.PasswordProtection(certificatePassword.toCharArray());

            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(certificateName, keyPassword);

            java.security.cert.Certificate cert = keyStore.getCertificate(certificateName);
            PublicKey publicKey = cert.getPublicKey();
            PrivateKey privateKey = privateKeyEntry.getPrivateKey();

            return new KeyPair(publicKey, privateKey);
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException | UnrecoverableEntryException e) {
            logger.error(e);
        }
        return null;
    }

    @Value("${encryption.keystore.password}")
    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    @Value("${encryption.certificate.password}")
    public void setCertificatePassword(String certificatePassword) {
        this.certificatePassword = certificatePassword;
    }

    @Value("${encryption.certificate.name}")
    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }
}
