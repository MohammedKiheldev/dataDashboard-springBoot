package com.DataDashboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DataDashboard.model.Person;
import com.DataDashboard.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    // Get all persons
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    // Get a person by ID
    public Optional<Person> getPersonById(Long id) {
        return personRepository.findById(id);
    }

    // Save a person
    public Person savePerson(Person person) {
        return personRepository.save(person);
    }

    // Delete a person by ID
    public void deletePerson(Long id) {
        personRepository.deleteById(id);
    }
}
