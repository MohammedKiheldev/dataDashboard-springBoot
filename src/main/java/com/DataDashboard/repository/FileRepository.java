package com.DataDashboard.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.DataDashboard.model.File;
import com.DataDashboard.model.Person;

public interface FileRepository extends JpaRepository<File, Long> {

    // Find a file by its name
    Optional<File> findByFileName(String fileName);

    // Find a file by its name and the associated user's ID
    Optional<File> findByFileNameAndPerson_Id(String fileName, Long userId);

    // Note: The return type and method name may need reconsideration
    Optional<Person> findAllByPerson(Person person);

    // Find all file names associated with a person using a custom query
    @Query("SELECT f.fileName FROM File f WHERE f.person = :person")
    List<String> findFileNamesByPerson(Person person);

    // Find all file names associated with a user by user ID using a custom query
    @Query("SELECT f.fileName FROM File f WHERE f.person.id = :userId")
    List<String> findFileNamesByUserId(@Param("userId") String userId);
}
