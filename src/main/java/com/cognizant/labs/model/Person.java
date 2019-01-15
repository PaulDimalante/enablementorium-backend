package com.cognizant.labs.model;

import com.cognizant.labs.model.converter.StringEncryptor;
import com.cognizant.labs.model.listener.PersonListener;

import javax.persistence.*;

@Entity
@EntityListeners(PersonListener.class)
public class Person {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column
    @Convert(converter = StringEncryptor.class)
    private String firstName;

    private String lastName;

    @Column(name="is_active")
    private boolean active = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
