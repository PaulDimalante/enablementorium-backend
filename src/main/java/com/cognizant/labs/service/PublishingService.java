package com.cognizant.labs.service;

import com.cognizant.labs.model.Person;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service
public class PublishingService {

    private static final Log logger = LogFactory.getLog(PublishingService.class);

    private Person person;

    public void sendUpdate(Person person) {
        logger.info("sending...");
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }

    public void reset() {
        this.person = null;
    }

}
