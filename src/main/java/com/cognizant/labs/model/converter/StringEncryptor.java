package com.cognizant.labs.model.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import static org.apache.commons.lang.StringUtils.isNotEmpty;

@Converter
public class StringEncryptor implements AttributeConverter<String,String> {

    @Autowired
    private CryptographyUtil cryptographyUtil;
    @Override
    public String convertToDatabaseColumn(String attribute)  {
        //check the attribute exists
        if (isNotEmpty(attribute)) {
            //encrypt
            SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
            return cryptographyUtil.encrypt(attribute);
        }//end if
        return null;//continue
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        //decrypt
        if (isNotEmpty(dbData)) {
            //encrypt
            SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
            return cryptographyUtil.decrypt(dbData);
        }//end if
        return null;
    }
}
