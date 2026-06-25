package org.skypro.hogwarts.service;

import org.skypro.hogwarts.model.Avatar;
import org.skypro.hogwarts.model.Student;
import org.skypro.hogwarts.repository.AvatarRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class AvatarService {

    private final AvatarRepository avatarRepository;
    private final StudentService studentService;

    // Путь для хранения аватарок
    private final Path avatarsDir = Paths.get("avatars");

    public AvatarService(AvatarRepository avatarRepository, StudentService studentService) {
        this.avatarRepository = avatarRepository;
        this.studentService = studentService;
    }

    /**
     * Загрузка аватарки для студента
     */
    public Avatar uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        Student student = studentService.getStudent(studentId);

        // Создаем папку, если её нет
        if (!Files.exists(avatarsDir)) {
            Files.createDirectories(avatarsDir);
        }

        // Генерируем имя файла
        String fileName = studentId + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = avatarsDir.resolve(fileName);

        // Сохраняем файл на диск
        Files.write(filePath, file.getBytes());

        // Создаем или обновляем аватарку
        Avatar avatar = avatarRepository.findByStudentId(studentId);
        if (avatar == null) {
            avatar = new Avatar();
            avatar.setStudent(student);
        }

        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());

        return avatarRepository.save(avatar);
    }

    /**
     * Получение аватарки по ID студента
     */
    public Avatar getAvatarByStudentId(Long studentId) {
        return avatarRepository.findByStudentId(studentId);
    }

    /**
     * Получение всех аватарок с пагинацией
     */
    public Page<Avatar> getAllAvatars(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return avatarRepository.findAll(pageable);
    }
}
