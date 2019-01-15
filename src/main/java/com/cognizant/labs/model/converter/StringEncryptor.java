package com.cognizant.labs.model.converter;

import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.crypto.Cipher;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import static org.apache.commons.lang.StringUtils.isNotEmpty;

@Converter
public class StringEncryptor implements AttributeConverter<String,String> {

    @Autowired
    private CryptographyUtil cryptographyUtil;

    public StringEncryptor() {
        this(new CryptographyUtil());
    }

    public StringEncryptor(CryptographyUtil cryptographyUtil) {
        this.cryptographyUtil = cryptographyUtil;
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        //check the attribute exists
        if (isNotEmpty(attribute)) {
            //encrypt
            SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
            Cipher cipher = cryptographyUtil.initializeCipher(Cipher.ENCRYPT_MODE);
            return cryptographyUtil.encrypt(cipher,attribute);
        }//end if
        return null;//continue
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        //decrypt
        if (isNotEmpty(dbData)) {
            //encrypt
            SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
            Cipher cipher = cryptographyUtil.initializeCipher(Cipher.DECRYPT_MODE);
            return cryptographyUtil.decrypt(cipher,dbData);
        }//end if
        return null;
    }
}
