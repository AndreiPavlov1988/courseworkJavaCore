package org.skypro.hogwarts.repository;

import org.skypro.hogwarts.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Дополнительные методы поиска (опционально)
    List<Student> findByName(String name);

    List<Student> findByAgeBetween(int minAge, int maxAge);
}
