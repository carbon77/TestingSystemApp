package com.example.testsys.models.question;

import androidx.annotation.NonNull;

public enum QuestionType {
    RADIO("Radio"), CHECKBOX("Checkbox");

    private final String name;

    QuestionType(String name) {
        this.name = name;
    }

    @NonNull
    public String toString() {
        return name;
    }
}
