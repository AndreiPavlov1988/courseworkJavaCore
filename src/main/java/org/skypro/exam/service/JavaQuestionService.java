package org.skypro.exam.service;

import org.skypro.exam.model.Question;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JavaQuestionService implements QuestionService {

    private final Set<Question> questions = new HashSet<>();
    private final Random random = new Random();

    // Инициализация с тестовыми вопросами (для удобства)
    public JavaQuestionService() {
        initializeQuestions();
    }

    private void initializeQuestions() {
        add("Что такое Java?", "Язык программирования");
        add("Что такое ООП?", "Объектно-ориентированное программирование");
        add("Что такое JVM?", "Java Virtual Machine");
        add("Что такое переменная?", "Область памяти для хранения данных");
        add("Что такое класс?", "Шаблон для создания объектов");
        add("Что такое наследование?", "Механизм для создания иерархии классов");
        add("Что такое полиморфизм?", "Способность объектов принимать разные формы");
        add("Что такое инкапсуляция?", "Скрытие внутреннего состояния объектов");
        add("Что такое интерфейс?", "Контракт для реализации методов");
        add("Что такое коллекция?", "Структура данных для хранения группы объектов");
    }

    @Override
    public Question add(String question, String answer) {
        Question newQuestion = new Question(question, answer);
        questions.add(newQuestion);
        return newQuestion;
    }

    @Override
    public Question add(Question question) {
        questions.add(question);
        return question;
    }

    @Override
    public Question remove(String question, String answer) {
        Question questionToRemove = new Question(question, answer);
        if (questions.remove(questionToRemove)) {
            return questionToRemove;
        }
        throw new IllegalArgumentException("Вопрос не найден: " + question);
    }

    @Override
    public Question remove(Question question) {
        if (questions.remove(question)) {
            return question;
        }
        throw new IllegalArgumentException("Вопрос не найден: " + question);
    }

    @Override
    public Collection<Question> getAll() {
        return new ArrayList<>(questions);
    }

    @Override
    public Question getRandomQuestion() {
        if (questions.isEmpty()) {
            throw new IllegalStateException("Нет доступных вопросов");
        }

        // Преобразуем Set в List для доступа по индексу
        List<Question> questionList = new ArrayList<>(questions);
        int randomIndex = random.nextInt(questionList.size());
        return questionList.get(randomIndex);
    }
}
