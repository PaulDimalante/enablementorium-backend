package com.cognizant.labs.service;

import com.cognizant.labs.model.Person;
import com.cognizant.labs.security.EncryptionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.persistence.Convert;
import java.lang.reflect.Field;
import java.util.stream.Stream;

@EnableBinding(StreamPublisherService.class)
@Service
public class PublishingService {

    private static final Log logger = LogFactory.getLog(PublishingService.class);

    @Autowired
    private EncryptionUtil encryptionUtil;

    @Autowired
    private StreamPublisherService streamPublisherService;

    @Autowired
    private ObjectMapper objectMapper;

    private Person person;

    public void sendUpdate(Person person) {
        //check for encryption
        person = (Person) checkEncryption(person);
        logger.info("sending...");
        try {
            streamPublisherService.publish().send(MessageBuilder.withPayload(objectMapper.writeValueAsString(person)).build());
        }
        catch (Exception e) {
            logger.error(e);
        }
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }

    public void reset() {
        this.person = null;
    }

    Object checkEncryption(Object person) {
        for (Field field : person.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (field.isAnnotationPresent(Convert.class)
                        && field.get(person) != null
                        && field.getType().isAssignableFrom(String.class)) {
                    field.set(person, encryptionUtil.encryptCertificate(field.get(person).toString()));
                }//end if
            }
            catch (IllegalAccessException e) {
                logger.error("Couldn't encrypt ",e);
            }
        }//end for
        return person;
    }
}
