package org.skypro.exam.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skypro.exam.model.Question;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class JavaQuestionServiceTest {

    private JavaQuestionService questionService;

    @BeforeEach
    void setUp() {
        questionService = new JavaQuestionService();
        // Очищаем тестовые данные для чистоты тестов
        questionService.getAll().clear();
    }

    @Test
    void add_shouldAddQuestion() {
        // Act
        Question result = questionService.add("Тестовый вопрос?", "Тестовый ответ");

        // Assert
        assertNotNull(result);
        assertEquals("Тестовый вопрос?", result.getQuestion());
        assertEquals("Тестовый ответ", result.getAnswer());
        assertEquals(1, questionService.getAll().size());
    }

    @Test
    void add_shouldNotAddDuplicate() {
        // Arrange
        questionService.add("Вопрос", "Ответ");

        // Act
        questionService.add("Вопрос", "Ответ");

        // Assert
        assertEquals(1, questionService.getAll().size());
    }

    @Test
    void remove_shouldRemoveQuestion() {
        // Arrange
        questionService.add("Вопрос", "Ответ");
        assertEquals(1, questionService.getAll().size());

        // Act
        Question removed = questionService.remove("Вопрос", "Ответ");

        // Assert
        assertNotNull(removed);
        assertEquals("Вопрос", removed.getQuestion());
        assertEquals("Ответ", removed.getAnswer());
        assertEquals(0, questionService.getAll().size());
    }

    @Test
    void remove_shouldThrowException_whenQuestionNotFound() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            questionService.remove("Несуществующий", "Ответ");
        });
    }

    @Test
    void getAll_shouldReturnAllQuestions() {
        // Arrange
        questionService.add("Вопрос 1", "Ответ 1");
        questionService.add("Вопрос 2", "Ответ 2");

        // Act
        Collection<Question> all = questionService.getAll();

        // Assert
        assertEquals(2, all.size());
    }

    @Test
    void getRandomQuestion_shouldReturnRandomQuestion() {
        // Arrange
        questionService.add("Вопрос 1", "Ответ 1");
        questionService.add("Вопрос 2", "Ответ 2");
        questionService.add("Вопрос 3", "Ответ 3");

        // Act
        Question random1 = questionService.getRandomQuestion();
        Question random2 = questionService.getRandomQuestion();

        // Assert
        assertNotNull(random1);
        assertNotNull(random2);
        // Хотя бы один из двух раз должен быть разным
        // или оба могут быть одинаковыми, это нормально для случайности
    }

    @Test
    void getRandomQuestion_shouldThrowException_whenNoQuestions() {
        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            questionService.getRandomQuestion();
        });
    }
}
