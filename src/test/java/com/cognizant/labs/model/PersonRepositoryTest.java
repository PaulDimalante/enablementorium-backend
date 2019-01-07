package com.cognizant.labs.model;

import org.junit.Test;

import java.util.Arrays;

import static org.mockito.Mockito.*;

public class PersonRepositoryTest {

    @Test
    public void testDeleteLoop() {
        Iterable<Person> persons = Arrays.asList(new Person());
        //mock
        PersonRepository personRepository = mock(PersonRepository.class);
        //mock the inner
        doCallRealMethod().when(personRepository).deleteAll(persons);
        //invoke
        personRepository.deleteAll(persons);
        //verify
        verify(personRepository,atLeastOnce()).deleteById(any());
    }

    @Test
    public void testDeletePerson() {
        Person person = new Person();
        //mock
        PersonRepository personRepository = mock(PersonRepository.class);
        //real
        doCallRealMethod().when(personRepository).delete(person);
        //invoke
        personRepository.delete(person);
        //verify
        verify(personRepository,atLeastOnce()).deleteById(any());
    }
}