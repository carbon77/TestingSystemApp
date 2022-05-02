package com.example.testsys.models.question;

public class Answer {
    private String text;
    private boolean correct;

    public Answer() {

    }

    public Answer(String text, boolean correct) {
        this.text = text;
        this.correct = correct;
    }

    public Answer(Answer answer) {
        text = answer.text;
        correct = answer.correct;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean getCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
