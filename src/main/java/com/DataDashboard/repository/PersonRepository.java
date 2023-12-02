package com.DataDashboard.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.DataDashboard.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {

    // Custom query method to find a person by email and password
    Optional<Person> findByEmailAndPassword(String email, String password);
   
}
