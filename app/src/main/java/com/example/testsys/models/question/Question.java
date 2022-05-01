package com.example.testsys.models.question;

import java.util.HashMap;
import java.util.Map;

public class Question {
    private String id;
    private String testId;
    private String text;
    private QuestionType type;
    private Map<String, Boolean> answers;

    public Question() {

    }

    public Question(String text, QuestionType type) {
        this.text = text;
        this.type = type;
        answers = new HashMap<>();
    }

    public void addAnswer(String answer, boolean isRight) {
        answers.put(answer, isRight);
    }

    public void setAnswer(int index, boolean newRight) {
        String key = (String) answers.keySet().toArray()[0];
        answers.put(key, newRight);
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

    public Map<String, Boolean> getAnswers() {
        return answers;
    }
}
