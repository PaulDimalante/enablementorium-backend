package com.cognizant.labs.model.listener;

import com.cognizant.labs.model.Person;
import com.cognizant.labs.model.PersonRepository;
import com.cognizant.labs.service.PublishingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles({"local","test"})
public class PersonListenerIT {

    @Autowired
    private PublishingService publishingService;

    @Autowired
    private PersonRepository personRepository;

    @Test
    public void testFirePublishingService() throws Exception {
        publishingService.reset();
        assertNull(publishingService.getPerson());
        //create a person
        Person person = new Person();
        person.setFirstName("john");
        //save
        personRepository.save(person);
        //check for post
        person = publishingService.getPerson();
        assertNotNull(person);
        assertNotNull(person.getFirstName());
        assertTrue(person.getFirstName().length() > "john".length());
        assertFalse(person.getFirstName().equals("john"));
    }

}