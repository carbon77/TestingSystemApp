package com.example.testsys.models.test;

public class Test {
    private String id;
    private String author;
    private String text;
    private int questionCount;
    private String creationDate;
    private String modificationDate;
    private int version;

    public Test() {

    }

    public Test(String id, String author, String title, int version, String creationDate, String modificationDate) {
        this.id = id;
        this.author = author;
        this.text = title;
        this.version = version;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.questionCount = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) { this.id = id; }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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
}
