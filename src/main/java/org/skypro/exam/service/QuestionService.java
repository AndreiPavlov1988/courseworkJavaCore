package org.skypro.exam.service;

import org.skypro.exam.model.Question;

import java.util.Collection;

/**
 * Интерфейс для работы с вопросами по определенному предмету
 */
public interface QuestionService {

    /**
     * Добавление вопроса
     * @param question текст вопроса
     * @param answer ответ на вопрос
     * @return добавленный вопрос
     */
    Question add(String question, String answer);

    /**
     * Добавление вопроса
     * @param question объект вопроса
     * @return добавленный вопрос
     */
    Question add(Question question);

    /**
     * Удаление вопроса
     * @param question текст вопроса
     * @param answer ответ на вопрос
     * @return удаленный вопрос
     */
    Question remove(String question, String answer);

    /**
     * Удаление вопроса
     * @param question объект вопроса
     * @return удаленный вопрос
     */
    Question remove(Question question);

    /**
     * Получение всех вопросов
     * @return коллекция всех вопросов
     */
    Collection<Question> getAll();

    /**
     * Получение случайного вопроса
     * @return случайный вопрос
     */
    Question getRandomQuestion();
}
