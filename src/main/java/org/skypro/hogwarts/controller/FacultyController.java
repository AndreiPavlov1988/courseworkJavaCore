package org.skypro.hogwarts.controller;

import org.skypro.hogwarts.model.Faculty;
import org.skypro.hogwarts.service.FacultyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/faculties")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public ResponseEntity<Faculty> createFaculty(@RequestBody Faculty faculty) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(facultyService.addFaculty(faculty));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Faculty> getFaculty(@PathVariable Long id) {
        return facultyService.getFaculty(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Faculty> getAllFaculties() {
        return facultyService.getAllFaculties();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Faculty> updateFaculty(@PathVariable Long id, @RequestBody Faculty faculty) {
        faculty.setId(id);
        Faculty updated = facultyService.updateFaculty(faculty);
        return updated != null
                ? ResponseEntity.ok(updated)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        if (facultyService.deleteFaculty(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Дополнительные эндпоинты (опционально)
    @GetMapping("/name/{name}")
    public List<Faculty> findByName(@PathVariable String name) {
        return facultyService.findByName(name);
    }

    @GetMapping("/color/{color}")
    public List<Faculty> findByColor(@PathVariable String color) {
        return facultyService.findByColor(color);
    }
}
