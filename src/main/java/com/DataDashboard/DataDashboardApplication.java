package com.DataDashboard;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.DataDashboard.model.File;
import com.DataDashboard.model.Person;
import com.DataDashboard.service.FileService;
import com.DataDashboard.service.PersonService;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class DataDashboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataDashboardApplication.class, args);
    }

 // DataDashboardApplication.java
    @Bean
    public CommandLineRunner loadData(FileService fileService,PersonService personService) {
        return args -> {
        	
        	// Create and save a person
            Person person = new Person();
            person.setName("kihel");
            person.setEmail("kihel@gmail.com");
            person.setPassword("0440");
            person.setSex("Male");

            personService.savePerson(person);
            
            // Provide the correct path to your CSV file
            String filePath = "C:\\Users\\kihel\\Downloads\\file.csv";

            // Read the content of the CSV file
            String fileContent = new String(Files.readAllBytes(Paths.get(filePath)));

            // Specify the personId associated with the file
            Long personId = 1L;  // Replace with the actual personId

            // Save the CSV file content using FileService
            File savedFile = fileService.saveCsvContent(fileContent, "file.csv", personId);

            System.out.println("CSV file loaded successfully with ID: " + savedFile.getId());
        };
    }


}
