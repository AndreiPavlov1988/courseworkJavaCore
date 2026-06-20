package org.skypro.exam.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.exam.model.Question;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExaminerServiceImplTest {

    @Mock
    private QuestionService questionService;

    @InjectMocks
    private ExaminerServiceImpl examinerService;

    private List<Question> testQuestions;

    @BeforeEach
    void setUp() {
        testQuestions = List.of(
                new Question("Вопрос 1", "Ответ 1"),
                new Question("Вопрос 2", "Ответ 2"),
                new Question("Вопрос 3", "Ответ 3"),
                new Question("Вопрос 4", "Ответ 4"),
                new Question("Вопрос 5", "Ответ 5")
        );
    }

    @Test
    void getQuestions_shouldReturnUniqueQuestions() {
        // Arrange
        when(questionService.getAll()).thenReturn(testQuestions);

        // Act
        Collection<Question> result = examinerService.getQuestions(3);

        // Assert
        assertEquals(3, result.size());
        // Проверяем, что все вопросы уникальны
        long distinctCount = result.stream().distinct().count();
        assertEquals(3, distinctCount);
        verify(questionService, times(1)).getAll();
    }

    @Test
    void getQuestions_shouldThrowException_whenAmountExceedsAvailable() {
        // Arrange
        when(questionService.getAll()).thenReturn(testQuestions);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> examinerService.getQuestions(10)
        );

        assertTrue(exception.getMessage().contains("10"));
        assertTrue(exception.getMessage().contains("5"));
        verify(questionService, times(1)).getAll();
    }

    @Test
    void getQuestions_shouldThrowException_whenAmountIsZero() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> examinerService.getQuestions(0)
        );

        assertTrue(exception.getMessage().contains("положительным"));
    }

    @Test
    void getQuestions_shouldThrowException_whenAmountIsNegative() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> examinerService.getQuestions(-5)
        );

        assertTrue(exception.getMessage().contains("положительным"));
    }

    @Test
    void getQuestions_shouldReturnAllQuestions_whenAmountEqualsAvailable() {
        // Arrange
        when(questionService.getAll()).thenReturn(testQuestions);

        // Act
        Collection<Question> result = examinerService.getQuestions(5);

        // Assert
        assertEquals(5, result.size());
        verify(questionService, times(1)).getAll();
    }

    @Test
    void getQuestions_shouldReturnOneQuestion_whenAmountIsOne() {
        // Arrange
        when(questionService.getAll()).thenReturn(testQuestions);

        // Act
        Collection<Question> result = examinerService.getQuestions(1);

        // Assert
        assertEquals(1, result.size());
        verify(questionService, times(1)).getAll();
    }
}
