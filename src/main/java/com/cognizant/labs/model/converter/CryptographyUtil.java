package com.cognizant.labs.model.converter;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.ArrayUtils;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.security.Provider;
import java.security.Security;


@Component("crypto")
public class CryptographyUtil {
    private static final Log logger = LogFactory.getLog(CryptographyUtil.class);

    @Value("${encryption.key}")
    private String key;

    @Value("${encryption.iv}")
    private String iv;

//    static {
//        boolean hasBC = false;
//        for (Provider provider : Security.getProviders()) {
//            if (provider.getName().equals(new BouncyCastleProvider().getName())) {
//                hasBC = true;
//                break;
//            }//end if
//        }//end for
//        if (!hasBC) {
//            Security.addProvider(new BouncyCastleProvider());
//        }//end if
//    }

    public String encrypt(String attribute)  {
        final byte [] info_plaintext = attribute.getBytes();
        final GCMBlockCipher gcm = new GCMBlockCipher(new AESEngine());
        final CipherParameters ivAndKey;
        try {
//            ivAndKey = new ParametersWithIV(new KeyParameter(Hex.decodeHex(key.toCharArray())), Hex.decodeHex(iv.toCharArray()));
            ivAndKey = new ParametersWithIV(new KeyParameter(key.getBytes()), iv.getBytes());
            gcm.init(true, ivAndKey);
            final byte [] inputBuf = new byte[gcm.getOutputSize(info_plaintext.length)];
            final int length1 = gcm.processBytes(info_plaintext, 0, info_plaintext.length, inputBuf, 0);
            final int length2 = gcm.doFinal(inputBuf, length1);
            final byte [] info_ciphertext = ArrayUtils.subarray(inputBuf, 0, length1 + length2);
            final String info_ciphertext_hex = new String(Hex.encodeHex(info_ciphertext));
            return info_ciphertext_hex;
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

}
