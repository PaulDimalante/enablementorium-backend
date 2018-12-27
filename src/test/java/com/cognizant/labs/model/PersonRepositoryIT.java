package com.cognizant.labs.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockserver.integration.ClientAndServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PersonRepositoryIT {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @Before
    public void before() throws Exception {
        personRepository.deleteAllInBatch();
    }

    @Test
    public void testRESTDelete() throws Exception {
        //create a person
        Person person = new Person();
        person.setFirstName("john");
        person.setLastName("doe");
        //save
        person = personRepository.save(person);
        Long id = person.getId();
        //now delete
        mvc.perform(delete("/persons/{id}",id).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
        //check that it's still there
        person = mapper.readValue(mvc.perform(get("/persons/{id}",id).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString(),Person.class);
        assertNotNull(person);
        assertFalse(person.isActive());
    }

    @Test
    public void testRESTSelect() throws Exception {
        //create a person
        //create a person
        Person person = new Person();
        person.setFirstName("john");
        person.setLastName("doe");
        //save
        person = personRepository.save(person);
        Long id = person.getId();
        //get the count
        MvcResult result = mvc.perform(get("/persons").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        Map<String,Object> response = mapper.readValue(result.getResponse().getContentAsString(),new TypeReference<Map<String,Object>>(){});
        Map<String,Object> page = (Map<String, Object>) response.get("page");
        assertTrue(((Integer) page.get("totalElements")) == 1);
        //now delete
        mvc.perform(delete("/persons/{id}",id).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
        //check that it's still there
        person = mapper.readValue(mvc.perform(get("/persons/{id}",id).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString(),Person.class);
        assertNotNull(person);
        assertFalse(person.isActive());
        //check the count change
        result = mvc.perform(get("/persons").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        response = mapper.readValue(result.getResponse().getContentAsString(),new TypeReference<Map<String,Object>>(){});
        page = (Map<String, Object>) response.get("page");
        assertTrue(((Integer) page.get("totalElements")) != 1);
    }

}