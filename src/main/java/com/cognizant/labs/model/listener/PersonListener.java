package com.cognizant.labs.model.listener;

import com.cognizant.labs.model.Person;
import com.cognizant.labs.service.PublishingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.persistence.PostPersist;

public class PersonListener {

    @Autowired
    private PublishingService publishingService;

    @PostPersist
    public void postUpdate(Person person) throws Exception {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        publishingService.sendUpdate(person);
    }
}
