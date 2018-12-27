package com.cognizant.labs.service;

import com.cognizant.labs.model.Person;
import org.junit.Test;

import static org.junit.Assert.*;

public class PublishingServiceTest {

    @Test
    public void testSend() throws Exception {
        PublishingService service = new PublishingService();
        assertNull(service.getPerson());
        service.sendUpdate(new Person());
        assertNotNull(service.getPerson());
    }

}