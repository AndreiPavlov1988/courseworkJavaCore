package org.skypro.hogwarts.webmvctest;

import org.junit.jupiter.api.Test;
import org.skypro.hogwarts.controller.StudentController;
import org.skypro.hogwarts.model.Student;
import org.skypro.hogwarts.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    // 1. Тест: GET /students - получение всех студентов
    @Test
    void getAllStudents_shouldReturnListOfStudents() throws Exception {
        // Arrange
        List<Student> students = Arrays.asList(
                new Student("Гарри Поттер", 17),
                new Student("Гермиона Грейнджер", 17)
        );
        when(studentService.getAllStudents()).thenReturn(students);

        // Act & Assert
        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Гарри Поттер"))
                .andExpect(jsonPath("$[1].name").value("Гермиона Грейнджер"));
    }

    // 2. Тест: POST /students - создание студента
    @Test
    void createStudent_shouldReturnCreatedStudent() throws Exception {
        // Arrange
        Student newStudent = new Student("Новый студент", 20);
        newStudent.setId(1L);
        when(studentService.addStudent(any(Student.class))).thenReturn(newStudent);

        // Act & Assert
        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Новый студент\",\"age\":20}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Новый студент"))
                .andExpect(jsonPath("$.age").value(20));
    }

    // 3. Тест: GET /students/{id} - получение студента по ID
    @Test
    void getStudentById_shouldReturnStudent_whenStudentExists() throws Exception {
        // Arrange
        Student student = new Student("Гарри Поттер", 17);
        student.setId(1L);
        when(studentService.getStudent(1L)).thenReturn(student);

        // Act & Assert
        mockMvc.perform(get("/students/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Гарри Поттер"))
                .andExpect(jsonPath("$.age").value(17));
    }

    @Test
    void getStudentById_shouldReturnNotFound_whenStudentDoesNotExist() throws Exception {
        // Arrange
        when(studentService.getStudent(999L))
                .thenThrow(new RuntimeException("Студент не найден"));

        // Act & Assert
        mockMvc.perform(get("/students/{id}", 999))
                .andExpect(status().isNotFound());
    }

    // 4. Тест: PUT /students/{id} - обновление студента
    @Test
    void updateStudent_shouldReturnUpdatedStudent() throws Exception {
        // Arrange
        Student updatedStudent = new Student("Обновленный студент", 25);
        updatedStudent.setId(1L);
        when(studentService.updateStudent(any(Student.class))).thenReturn(updatedStudent);

        // Act & Assert
        mockMvc.perform(put("/students/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Обновленный студент\",\"age\":25}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Обновленный студент"))
                .andExpect(jsonPath("$.age").value(25));
    }

    // 5. Тест: DELETE /students/{id} - удаление студента
    @Test
    void deleteStudent_shouldReturnNoContent() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/students/{id}", 1))
                .andExpect(status().isNoContent());
    }

    // 6. Тест: GET /students/age-between - поиск по возрасту
    @Test
    void getStudentsByAgeBetween_shouldReturnStudentsInAgeRange() throws Exception {
        // Arrange
        List<Student> students = Arrays.asList(
                new Student("Студент 18 лет", 18),
                new Student("Студент 20 лет", 20)
        );
        when(studentService.getStudentsByAgeBetween(18, 25)).thenReturn(students);

        // Act & Assert
        mockMvc.perform(get("/students/age-between")
                        .param("minAge", "18")
                        .param("maxAge", "25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // 7. Тест: GET /students/{id}/faculty - получение факультета студента
    @Test
    void getStudentFaculty_shouldReturnFaculty() throws Exception {
        // Arrange
        // Мокаем возврат факультета (если есть)
        when(studentService.getStudent(1L)).thenReturn(new Student("Студент", 20));

        // Act & Assert
        mockMvc.perform(get("/students/{id}/faculty", 1))
                .andExpect(status().isOk());
        // Может быть 404 или 200, зависит от наличия факультета
    }
}
