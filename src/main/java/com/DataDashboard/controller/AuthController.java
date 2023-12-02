package com.DataDashboard.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.DataDashboard.model.Person;
import com.DataDashboard.repository.PersonRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/login")  // Base path for Swagger documentation
@Api(tags = "Authentication API")
public class AuthController {

    @Autowired
    private PersonRepository personRepository;

    @PostMapping()
    @ApiOperation("Authenticate a user")
    public ResponseEntity<Object> signIn(@RequestBody Person signInRequest) {
        Optional<Person> optionalPerson = personRepository.findByEmailAndPassword(signInRequest.getEmail(), signInRequest.getPassword());

        if (optionalPerson.isPresent()) {
            Person authenticatedPerson = optionalPerson.get();
            Map<String, Object> response = new HashMap<>();
            response.put("id", authenticatedPerson.getId());
            response.put("message", "Sign in successful!");
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Invalid credentials"));
        }
    }
}
