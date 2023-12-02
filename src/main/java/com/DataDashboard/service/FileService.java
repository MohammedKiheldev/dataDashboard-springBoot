package com.DataDashboard.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.DataDashboard.model.File;
import com.DataDashboard.model.Person;
import com.DataDashboard.repository.FileRepository;
import com.DataDashboard.repository.PersonRepository;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private PersonRepository personRepository;

    // Save CSV content to a new file associated with a person
    public File saveCsvContent(String content, String fileName, Long personId) {
        // Validate content if needed

        File fileEntity = new File();
        fileEntity.setFileName(fileName);
        fileEntity.setData(content);

        // Retrieve the person from the database
        Optional<Person> personOptional = personRepository.findById(personId);
        personOptional.ifPresent(fileEntity::setPerson);

        return fileRepository.save(fileEntity);
    }

    // Save CSV file uploaded via MultipartFile
    public File saveCsvFile(MultipartFile file, String userId) {
        try {
            validateCsvFile(file);

            File fileEntity = new File();
            fileEntity.setFileName(StringUtils.cleanPath(file.getOriginalFilename()));
            fileEntity.setPerson(personRepository.getById(Long.parseLong(userId)));

            // Convert the file to a string
            String fileContent = new String(file.getBytes(), StandardCharsets.UTF_8);
            fileEntity.setData(fileContent);

            return fileRepository.save(fileEntity);
        } catch (IOException e) {
            throw new RuntimeException("Error reading the file", e);
        }
    }

    // Validate CSV file
    public void validateCsvFile(MultipartFile file) {
        // Implement any validation logic for the CSV file if needed
        // For example, check file size, file type, etc.
        if (!file.getOriginalFilename().toLowerCase().endsWith(".csv")) {
            throw new IllegalArgumentException("Only CSV files are allowed.");
        }
    }

    // Get file content by name and user ID
    public Optional<String> getFileContentByName(String fileName, Long userId) {
        Optional<File> fileEntityOptional = fileRepository.findByFileNameAndPerson_Id(fileName, userId);

        fileEntityOptional.ifPresent(fileEntity -> {
            // Display the file name
            System.out.println("File Name: " + fileEntity.getFileName());

            // Display the file content
            System.out.println("File Content: " + fileEntity.getData());
        });

        return fileEntityOptional.map(File::getData);
    }

    // Update file content by file name
    public boolean updateFileContent(String fileName, String newContent) {
        Optional<File> fileOptional = fileRepository.findByFileName(fileName);

        if (fileOptional.isPresent()) {
            File file = fileOptional.get();
            file.setData(newContent);
            fileRepository.save(file);
            return true;
        }

        return false;
    }

    // Get file names associated with a user
    public List<String> getUserFileNames(String userId) {
        // Implement logic to get file names associated with the user
        return fileRepository.findFileNamesByUserId(userId);
    }
}
