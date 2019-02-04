package com.cognizant.labs.model.projections;

import com.cognizant.labs.model.Person;
import com.cognizant.labs.model.PersonRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@ActiveProfiles({"test"})
@SpringBootTest
public class PersonProjectionIT {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mvc;

    @Before
    public void before() throws Exception {
        personRepository.deleteAllInBatch();//clean up
    }

    @Test
    public void testHasId() throws Exception {
        //create a person
        Person person = new Person();
        person.setFirstName("john");
        person.setLastName("doe");
        //save it
        mvc.perform(post("/persons").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(person))).andExpect(status().isCreated());
        //retrieve all
        MvcResult result = mvc.perform(get("/persons").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        Map<String,Object> payload = mapper.readValue(result.getResponse().getContentAsString(),new TypeReference<Map<String,Object>>(){});
        //check that there's an ID in the JSON object
        Map<String,Object> embedded = (Map<String, Object>) payload.get("_embedded");
        List<Map<String,Object>> persons = (List<Map<String, java.lang.Object>>) embedded.get("persons");
        for (Map<String,Object> p : persons) {
            assertTrue(p.containsKey("id"));
        }//end for
    }
}