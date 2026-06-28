package org.skypro.hogwarts.controller;

import org.skypro.hogwarts.model.Faculty;
import org.skypro.hogwarts.model.Student;
import org.skypro.hogwarts.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Существующие эндпоинты
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Student createStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id) {
        return studentService.getStudent(id);
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/age-between")
    public List<Student> getStudentsByAgeBetween(
            @RequestParam int minAge,
            @RequestParam int maxAge) {
        return studentService.getStudentsByAgeBetween(minAge, maxAge);
    }

    @GetMapping("/{id}/faculty")
    public Faculty getStudentFaculty(@PathVariable Long id) {
        return studentService.getStudentFaculty(id);
    }

    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student student) {
        student.setId(id);
        return studentService.updateStudent(student);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }

    // ================================================
    // НОВЫЕ ЭНДПОИНТЫ ДЛЯ ШАГА 1
    // ================================================

    // 1. Количество всех студентов
    @GetMapping("/count")
    public long getStudentsCount() {
        return studentService.getStudentsCount();
    }

    // 2. Средний возраст студентов
    @GetMapping("/average-age")
    public double getAverageAge() {
        return studentService.getAverageAge();
    }

    // 3. Последние 5 студентов
    @GetMapping("/last-five")
    public List<Student> getLastFiveStudents() {
        return studentService.getLastFiveStudents();
    }

    @GetMapping("/names-starting-with-a")
    public List<String> getStudentNamesStartingWithA() {
        return studentService.getStudentNamesStartingWithA();}

    @GetMapping("/average-age-stream")
    public double getAverageAgeStream() {
        return studentService.getAverageAgeStream();}

    @GetMapping("/print-parallel")
    public String printStudentsParallel() {
        studentService.printStudentsParallel();
        return "Параллельный вывод студентов выполнен. Проверьте консоль.";
    }
    @GetMapping("/print-synchronized")
    public String printStudentsSynchronized() {
        studentService.printStudentsSynchronized();
        return "Синхронизированный вывод студентов выполнен. Проверьте консоль.";
    }
}
