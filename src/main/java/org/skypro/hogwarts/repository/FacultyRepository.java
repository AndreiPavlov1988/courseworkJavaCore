package org.skypro.hogwarts.repository;

import org.skypro.hogwarts.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    // Найти факультет по имени (игнорируя регистр)
    List<Faculty> findByNameIgnoreCase(String name);

    // Найти факультет по цвету (игнорируя регистр)
    List<Faculty> findByColorIgnoreCase(String color);

    // Найти факультет по имени ИЛИ цвету (игнорируя регистр)
    List<Faculty> findByNameIgnoreCaseOrColorIgnoreCase(String name, String color);

    // Найти факультет по ID студента (через связь)
    @Query("SELECT f FROM Faculty f JOIN f.students s WHERE s.id = :studentId")
    Optional<Faculty> findFacultyByStudentId(@Param("studentId") Long studentId);
}
