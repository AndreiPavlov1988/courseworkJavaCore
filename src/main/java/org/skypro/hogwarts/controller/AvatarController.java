package org.skypro.hogwarts.controller;

import org.skypro.hogwarts.model.Avatar;
import org.skypro.hogwarts.service.AvatarService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/avatars")
public class AvatarController {

    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    /**
     * Загрузка аватарки для студента
     * POST /avatars?studentId=1
     */
    @PostMapping
    public ResponseEntity<Avatar> uploadAvatar(
            @RequestParam Long studentId,
            @RequestParam MultipartFile file) throws IOException {
        Avatar avatar = avatarService.uploadAvatar(studentId, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(avatar);
    }

    /**
     * Получение аватарки по ID студента (только данные)
     * GET /avatars/student/1/data
     */
    @GetMapping("/student/{studentId}/data")
    public ResponseEntity<byte[]> getAvatarData(@PathVariable Long studentId) {
        Avatar avatar = avatarService.getAvatarByStudentId(studentId);
        if (avatar == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getFileSize());

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body(avatar.getData());
    }

    /**
     * Получение всех аватарок с пагинацией
     * GET /avatars?page=0&size=10
     */
    @GetMapping
    public Page<Avatar> getAvatars(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return avatarService.getAllAvatars(page, size);
    }
}
