package org.skypro.hogwarts.integration;

import org.junit.jupiter.api.Test;
import org.skypro.hogwarts.model.Faculty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/faculties";
    }

    // 1. Тест: GET /faculties - получение всех факультетов
    @Test
    void getAllFaculties_shouldReturnListOfFaculties() {
        // Arrange
        String url = getBaseUrl();

        // Act
        ResponseEntity<List<Faculty>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {}
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    // 2. Тест: POST /faculties - создание факультета
    @Test
    void createFaculty_shouldReturnCreatedFaculty() {
        // Arrange
        String url = getBaseUrl();
        Faculty newFaculty = new Faculty("Тестовый факультет", "Красный");

        // Act
        ResponseEntity<Faculty> response = restTemplate.postForEntity(
                url,
                newFaculty,
                Faculty.class
        );

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals("Тестовый факультет", response.getBody().getName());
        assertEquals("Красный", response.getBody().getColor());
    }

    // 3. Тест: GET /faculties/{id} - получение факультета по ID
    @Test
    void getFacultyById_shouldReturnFaculty_whenFacultyExists() {
        // Arrange
        Faculty newFaculty = new Faculty("Факультет для поиска", "Синий");
        ResponseEntity<Faculty> createResponse = restTemplate.postForEntity(
                getBaseUrl(),
                newFaculty,
                Faculty.class
        );
        Long facultyId = createResponse.getBody().getId();

        // Act
        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                getBaseUrl() + "/" + facultyId,
                Faculty.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(facultyId, response.getBody().getId());
        assertEquals("Факультет для поиска", response.getBody().getName());
    }

    @Test
    void getFacultyById_shouldReturnNotFound_whenFacultyDoesNotExist() {
        // Arrange
        Long nonExistentId = 99999L;

        // Act
        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                getBaseUrl() + "/" + nonExistentId,
                Faculty.class
        );

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // 4. Тест: PUT /faculties/{id} - обновление факультета
    @Test
    void updateFaculty_shouldReturnUpdatedFaculty() {
        // Arrange
        Faculty newFaculty = new Faculty("Старое название", "Старый цвет");
        ResponseEntity<Faculty> createResponse = restTemplate.postForEntity(
                getBaseUrl(),
                newFaculty,
                Faculty.class
        );
        Long facultyId = createResponse.getBody().getId();

        Faculty updatedFaculty = new Faculty("Новое название", "Новый цвет");
        HttpEntity<Faculty> requestEntity = new HttpEntity<>(updatedFaculty);

        // Act
        ResponseEntity<Faculty> response = restTemplate.exchange(
                getBaseUrl() + "/" + facultyId,
                HttpMethod.PUT,
                requestEntity,
                Faculty.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Новое название", response.getBody().getName());
        assertEquals("Новый цвет", response.getBody().getColor());
    }

    // 5. Тест: DELETE /faculties/{id} - удаление факультета
    @Test
    void deleteFaculty_shouldReturnNoContent_whenFacultyExists() {
        // Arrange
        Faculty newFaculty = new Faculty("Факультет для удаления", "Зеленый");
        ResponseEntity<Faculty> createResponse = restTemplate.postForEntity(
                getBaseUrl(),
                newFaculty,
                Faculty.class
        );
        Long facultyId = createResponse.getBody().getId();

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(
                getBaseUrl() + "/" + facultyId,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Проверяем, что факультет действительно удален
        ResponseEntity<Faculty> getResponse = restTemplate.getForEntity(
                getBaseUrl() + "/" + facultyId,
                Faculty.class
        );
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    // 6. Тест: GET /faculties/search - поиск факультета по имени/цвету
    @Test
    void searchFaculties_shouldReturnFacultiesByQuery() {
        // Arrange
        String url = getBaseUrl() + "/search?query=Гриффиндор";

        // Act
        ResponseEntity<List<Faculty>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {}
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    // 7. Тест: GET /faculties/{id}/students - получение студентов факультета
    @Test
    void getFacultyStudents_shouldReturnStudentsList() {
        // Arrange - нужен факультет
        Faculty newFaculty = new Faculty("Факультет со студентами", "Желтый");
        ResponseEntity<Faculty> createResponse = restTemplate.postForEntity(
                getBaseUrl(),
                newFaculty,
                Faculty.class
        );
        Long facultyId = createResponse.getBody().getId();

        // Act
        ResponseEntity<List> response = restTemplate.getForEntity(
                getBaseUrl() + "/" + facultyId + "/students",
                List.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        // Студентов может не быть, но список должен быть пустым
    }
}
