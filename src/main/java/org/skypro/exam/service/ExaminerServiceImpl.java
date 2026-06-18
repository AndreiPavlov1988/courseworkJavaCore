package org.skypro.exam.service;

import org.skypro.exam.model.Question;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ExaminerServiceImpl implements ExaminerService {

    private final QuestionService questionService;
    private final Random random = new Random();

    public ExaminerServiceImpl(QuestionService questionService) {
        this.questionService = questionService;
    }

    @Override
    public Collection<Question> getQuestions(int amount) {
        Collection<Question> allQuestions = questionService.getAll();
        int availableCount = allQuestions.size();

        // Проверяем, достаточно ли вопросов
        if (amount > availableCount) {
            throw new IllegalArgumentException(
                    "Запрошено " + amount + " вопросов, но доступно только " + availableCount
            );
        }

        if (amount <= 0) {
            throw new IllegalArgumentException("Количество вопросов должно быть положительным");
        }

        // Выбираем уникальные случайные вопросы
        Set<Question> selectedQuestions = new HashSet<>();
        List<Question> questionList = new ArrayList<>(allQuestions);

        while (selectedQuestions.size() < amount) {
            int randomIndex = random.nextInt(questionList.size());
            selectedQuestions.add(questionList.get(randomIndex));
        }

        return new ArrayList<>(selectedQuestions);
    }
}
