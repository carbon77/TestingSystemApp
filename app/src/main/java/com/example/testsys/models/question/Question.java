package com.example.testsys.models.question;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private String id;
    private String testId;
    private String text;
    private QuestionType type;
    private List<Answer> answers;

    public Question() {

    }

    public Question(String text, QuestionType type) {
        this.text = text;
        this.type = type;
        answers = new ArrayList<>();
    }

    public void addAnswer(String text, boolean correct) {
        answers.add(new Answer(text, correct));
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

    public List<Answer> getAnswers() {
        return answers;
    }

    public static class Answer {
        private String text;
        private boolean correct;

        public Answer() {

        }

        public Answer(String text, boolean correct) {
            this.text = text;
            this.correct = correct;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public boolean isCorrect() {
            return correct;
        }

        public void setCorrect(boolean correct) {
            this.correct = correct;
        }
    }
}
