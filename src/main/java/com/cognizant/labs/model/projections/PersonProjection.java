package com.cognizant.labs.model.projections;

import com.cognizant.labs.model.Person;
import org.springframework.data.rest.core.config.Projection;

@Projection(name="full", types={Person.class})
public interface PersonProjection {

    Long getId();

    String getFirstName();

    String getLastName();

}
