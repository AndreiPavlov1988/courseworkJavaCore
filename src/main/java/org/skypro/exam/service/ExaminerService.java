package org.skypro.exam.service;

import org.skypro.exam.model.Question;

import java.util.Collection;

/**
 * Интерфейс для получения случайных вопросов
 */
public interface ExaminerService {

    /**
     * Получение указанного количества случайных уникальных вопросов
     * @param amount количество вопросов
     * @return коллекция уникальных вопросов
     * @throws IllegalArgumentException если запрошено больше вопросов, чем есть в сервисе
     */
    Collection<Question> getQuestions(int amount);
}
