package org.skypro.hogwarts.repository;

import org.skypro.hogwarts.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Найти студентов по возрасту между min и max
    List<Student> findByAgeBetween(int minAge, int maxAge);

    // Найти студентов по имени (игнорируя регистр)
    List<Student> findByNameContainingIgnoreCase(String name);

    // Найти студента по ID факультета
    List<Student> findByFacultyId(Long facultyId);
}
