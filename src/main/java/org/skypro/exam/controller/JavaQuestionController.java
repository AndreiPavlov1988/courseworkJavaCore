package org.skypro.exam.controller;

import org.skypro.exam.model.Question;
import org.skypro.exam.service.QuestionService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/exam/java")
public class JavaQuestionController {

    private final QuestionService questionService;

    public JavaQuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping
    public Collection<Question> getAllQuestions() {
        return questionService.getAll();
    }

    @GetMapping("/add")
    public Question addQuestion(
            @RequestParam String question,
            @RequestParam String answer) {
        return questionService.add(question, answer);
    }

    @GetMapping("/remove")
    public Question removeQuestion(
            @RequestParam String question,
            @RequestParam String answer) {
        return questionService.remove(question, answer);
    }
}
