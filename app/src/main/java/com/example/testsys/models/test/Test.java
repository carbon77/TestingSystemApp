package com.example.testsys.models.test;

import com.example.testsys.models.user.User;
import com.example.testsys.utils.DateService;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Test {
    private String id;
    private String userId;
    private String userUsername;
    private String text;
    private int questionCount;
    private String creationDate;
    private String modificationDate;
    private int version;

    public Test() {

    }

    public Test(String id, String author, String title) {
        this.id = id;
        this.userId = author;
        this.text = title;
        this.questionCount = 0;
    }

    public Test(User user) {
        Calendar today = new GregorianCalendar();
        this.userId = user.getId();
        this.userUsername = user.getUsername();
        this.questionCount = 0;
        this.creationDate = DateService.fromCalendar(today);
        this.modificationDate = DateService.fromCalendar(today);
        this.version = 1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) { this.id = id; }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(String modificationDate) {
        this.modificationDate = modificationDate;
    }

    public String getUserUsername() {
        return userUsername;
    }

    public void setUserUsername(String userUsername) {
        this.userUsername = userUsername;
    }
}
