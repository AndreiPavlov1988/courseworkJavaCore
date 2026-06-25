package org.skypro.hogwarts.repository;

import org.skypro.hogwarts.model.Avatar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Long> {

    // Поиск аватарки по студенту
    Avatar findByStudentId(Long studentId);

    // Пагинация: findAll с Pageable уже есть в JpaRepository
    // Page<Avatar> findAll(Pageable pageable);  // уже доступен
}
