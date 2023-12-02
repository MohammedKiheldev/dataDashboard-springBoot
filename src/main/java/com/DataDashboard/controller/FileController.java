package com.DataDashboard.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.DataDashboard.model.File;
import com.DataDashboard.service.FileService;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/files")  // Base path for Swagger documentation
@Api(tags = "File API")
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping("/user/{userId}")
    @ApiOperation("Get file names for a user")
    public ResponseEntity<List<String>> getUserFileNames(@PathVariable String userId) {
        try {
            List<String> userFileNames = fileService.getUserFileNames(userId);

            if (!userFileNames.isEmpty()) {
                return ResponseEntity.ok(userFileNames);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ArrayList<>());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }

    @PostMapping("/upload")
    @ApiOperation("Upload a CSV file")
    public ResponseEntity<String> handleFileUpload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "userId", required = false) String userId) {
        try {
            if (userId == null) {
                return ResponseEntity.badRequest().body("User ID is required.");
            }

            fileService.validateCsvFile(file);  // Added for Swagger documentation

            // Updated endpoint path for Swagger documentation
            File savedFile = fileService.saveCsvFile(file, userId);
            return ResponseEntity.ok("CSV file uploaded successfully with ID: " + savedFile.getId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{fileName}")
    @ApiOperation("Update file content")
    public ResponseEntity<String> updateFileContent(@PathVariable String fileName, @RequestBody String newContent) {
        try {
            boolean updateSuccess = fileService.updateFileContent(fileName, newContent);

            if (updateSuccess) {
                return ResponseEntity.ok("File content updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating file content");
        }
    }

    @GetMapping("/{fileName}")
    @ApiOperation("Get file content")
    public ResponseEntity<String> getFileContent(@PathVariable String fileName, @RequestParam(value = "userId", required = false) Long userId) {
        try {
            // Updated endpoint path for Swagger documentation
            return fileService.getFileContentByName(fileName, userId)
                    .map(content -> {
                        HttpHeaders responseHeaders = new HttpHeaders();
                        responseHeaders.set("Content-Type", "text/plain");
                        return new ResponseEntity<>(content, responseHeaders, HttpStatus.OK);
                    })
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving file content");
        }
    }
}
