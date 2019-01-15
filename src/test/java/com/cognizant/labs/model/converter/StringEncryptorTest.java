package com.cognizant.labs.model.converter;

import com.sun.org.apache.xpath.internal.operations.String;
import org.junit.Test;

import static org.junit.Assert.*;

public class StringEncryptorTest {

    @Test
    public void convertToDatabaseColumn() {
        assertNotNull(new StringEncryptor().convertToDatabaseColumn("hello"));

    }

    @Test
    public void convertToEntityAttribute() {
    }
}