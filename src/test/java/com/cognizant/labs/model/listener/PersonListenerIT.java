package com.cognizant.labs.model.listener;

import com.cognizant.labs.model.Person;
import com.cognizant.labs.model.PersonRepository;
import com.cognizant.labs.service.PublishingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonListenerIT {

    @Autowired
    private PublishingService publishingService;

    @Autowired
    private PersonRepository personRepository;

    @Test
    public void testFirePublishingService() throws Exception {
        assertNull(publishingService.getPerson());
        //create a person
        Person person = new Person();
        person.setFirstName("john");
        //save
        personRepository.save(person);
        //check for post
        assertNotNull(publishingService.getPerson());
    }

}