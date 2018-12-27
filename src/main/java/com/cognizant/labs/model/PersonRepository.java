package com.cognizant.labs.model;

import com.cognizant.labs.model.projections.PersonProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;

@RepositoryRestResource(excerptProjection = PersonProjection.class)
public interface PersonRepository extends JpaRepository<Person,Long> {

    @Query("select p from #{#entityName} p where p.active = true")
    @Override
    Page<Person> findAll(Pageable pageable);

    @Query("select count(p) from #{#entityName} p where p.active = true")
    @Override
    long count();

    @RestResource(exported = false)
    @Override
    void deleteInBatch(Iterable<Person> entities);

    @RestResource(exported = false)
    @Override
    void deleteAllInBatch();

    @RestResource(exported = false)
    @Override
    @Query("update #{#entityName} p set p.active = false where p.id = ?1")
    @Transactional
    @Modifying
    void deleteById(Long aLong);

    @Override
    default void delete(Person entity) {
        deleteById(entity.getId());
    }

    @RestResource(exported = false)
    @Override
    default void deleteAll(Iterable<? extends Person> entities) {
        for (Person person : entities) {
            deleteById(person.getId());
        }//end for
    }

    @RestResource(exported = false)
    @Override
    void deleteAll();
}
