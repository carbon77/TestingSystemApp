package com.example.testsys.models.question;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class Question {
    private String id;
    private String testId;
    private String text;
    private QuestionType type;
    private Map<String, Answer> answers;
    private int order;
    private int score;

    public Question() {

    }

    public Question(String text, QuestionType type) {
        this.text = text;
        this.type = type;
        answers = new HashMap<>();
    }

    public Question(Question question) {
        id = question.id;
        testId = question.testId;
        text = question.text;
        type = question.type;
        order = question.order;
        score = question.score;
        answers = new HashMap<>();

        question.answers.forEach((s, answer) -> {
            answers.put(s, new Answer(answer));
        });
    }

    public void addAnswer(String text, boolean correct) {
        if (answers.size() == 0) {
            answers.put("0_key", new Answer(text, correct));
        } else {
            String key = answers.size() + "_key";
            answers.put(key, new Answer(text, correct));
        }
    }

    public void setAnswer(int index, boolean newCorrect) {
        answers.get(index).setCorrect(newCorrect);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getTestId() {
        return testId;
    }

    public QuestionType getType() {
        return type;
    }

    public String getTypeName() {
        return type.toString();
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Map<String, Answer> getAnswers() {
        return answers;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
