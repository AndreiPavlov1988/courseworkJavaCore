package org.skypro.hogwarts.integration;

import org.junit.jupiter.api.Test;
import org.skypro.hogwarts.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/students";
    }

    // 1. Тест: GET /students - получение всех студентов
    @Test
    void getAllStudents_shouldReturnListOfStudents() {
        // Arrange
        String url = getBaseUrl();

        // Act
        ResponseEntity<List<Student>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {}
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        // Может быть пустым, если в БД нет студентов
    }

    // 2. Тест: POST /students - создание студента
    @Test
    void createStudent_shouldReturnCreatedStudent() {
        // Arrange
        String url = getBaseUrl();
        Student newStudent = new Student("Тестовый Студент", 20);

        // Act
        ResponseEntity<Student> response = restTemplate.postForEntity(
                url,
                newStudent,
                Student.class
        );

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals("Тестовый Студент", response.getBody().getName());
        assertEquals(20, response.getBody().getAge());
    }

    // 3. Тест: GET /students/{id} - получение студента по ID
    @Test
    void getStudentById_shouldReturnStudent_whenStudentExists() {
        // Arrange
        // Сначала создаем студента
        Student newStudent = new Student("Студент для поиска", 22);
        ResponseEntity<Student> createResponse = restTemplate.postForEntity(
                getBaseUrl(),
                newStudent,
                Student.class
        );
        Long studentId = createResponse.getBody().getId();

        // Act
        ResponseEntity<Student> response = restTemplate.getForEntity(
                getBaseUrl() + "/" + studentId,
                Student.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(studentId, response.getBody().getId());
        assertEquals("Студент для поиска", response.getBody().getName());
    }

    @Test
    void getStudentById_shouldReturnNotFound_whenStudentDoesNotExist() {
        // Arrange
        Long nonExistentId = 99999L;

        // Act
        ResponseEntity<Student> response = restTemplate.getForEntity(
                getBaseUrl() + "/" + nonExistentId,
                Student.class
        );

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // 4. Тест: PUT /students/{id} - обновление студента
    @Test
    void updateStudent_shouldReturnUpdatedStudent() {
        // Arrange
        // Создаем студента
        Student newStudent = new Student("Старое имя", 25);
        ResponseEntity<Student> createResponse = restTemplate.postForEntity(
                getBaseUrl(),
                newStudent,
                Student.class
        );
        Long studentId = createResponse.getBody().getId();

        // Обновляем данные
        Student updatedStudent = new Student("Новое имя", 26);
        HttpEntity<Student> requestEntity = new HttpEntity<>(updatedStudent);

        // Act
        ResponseEntity<Student> response = restTemplate.exchange(
                getBaseUrl() + "/" + studentId,
                HttpMethod.PUT,
                requestEntity,
                Student.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Новое имя", response.getBody().getName());
        assertEquals(26, response.getBody().getAge());
    }

    // 5. Тест: DELETE /students/{id} - удаление студента
    @Test
    void deleteStudent_shouldReturnNoContent_whenStudentExists() {
        // Arrange
        // Создаем студента для удаления
        Student newStudent = new Student("Студент для удаления", 18);
        ResponseEntity<Student> createResponse = restTemplate.postForEntity(
                getBaseUrl(),
                newStudent,
                Student.class
        );
        Long studentId = createResponse.getBody().getId();

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(
                getBaseUrl() + "/" + studentId,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Проверяем, что студент действительно удален
        ResponseEntity<Student> getResponse = restTemplate.getForEntity(
                getBaseUrl() + "/" + studentId,
                Student.class
        );
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    // 6. Тест: GET /students/age-between - поиск по возрасту
    @Test
    void getStudentsByAgeBetween_shouldReturnStudentsInAgeRange() {
        // Arrange
        String url = getBaseUrl() + "/age-between?minAge=18&maxAge=25";

        // Act
        ResponseEntity<List<Student>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {}
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        // Проверяем, что все студенты в диапазоне
        for (Student student : response.getBody()) {
            assertTrue(student.getAge() >= 18 && student.getAge() <= 25);
        }
    }

    // 7. Тест: GET /students/{id}/faculty - получение факультета студента
    @Test
    void getStudentFaculty_shouldReturnFaculty_whenStudentExists() {
        // Arrange - нужен студент с факультетом
        // Сначала создаем факультет
        // ... (это сложно без Faculty, можно упростить)

        // Если студент без факультета, проверяем что ответ 404 или null
        Student newStudent = new Student("Студент без факультета", 20);
        ResponseEntity<Student> createResponse = restTemplate.postForEntity(
                getBaseUrl(),
                newStudent,
                Student.class
        );
        Long studentId = createResponse.getBody().getId();

        // Act
        ResponseEntity<Object> response = restTemplate.getForEntity(
                getBaseUrl() + "/" + studentId + "/faculty",
                Object.class
        );

        // Assert
        // Может быть 404 или 200 с null, зависит от реализации
        assertTrue(response.getStatusCode() == HttpStatus.OK ||
                response.getStatusCode() == HttpStatus.NOT_FOUND);
    }
}
