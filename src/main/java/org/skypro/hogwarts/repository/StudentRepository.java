package org.skypro.hogwarts.repository;

import org.skypro.hogwarts.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Существующие методы
    List<Student> findByAgeBetween(int minAge, int maxAge);
    List<Student> findByNameContainingIgnoreCase(String name);

    // 1. Количество всех студентов в школе
    @Query("SELECT COUNT(s) FROM Student s")
    long countAllStudents();

    // 2. Средний возраст студентов
    @Query("SELECT AVG(s.age) FROM Student s")
    double getAverageAge();

    // 3. Получить последних 5 студентов (с максимальным ID)
    @Query(value = "SELECT * FROM students ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Student> findLastFiveStudents();
}
