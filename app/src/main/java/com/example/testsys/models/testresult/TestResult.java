package com.example.testsys.models.testresult;

import com.example.testsys.models.question.Answer;
import com.example.testsys.models.question.Question;
import com.example.testsys.models.question.QuestionType;
import com.example.testsys.models.test.Test;
import com.example.testsys.utils.DateService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestResult {
    private String id;
    private String testId;
    private String userId;
    private String passingDate;
    private float totalScores;
    private boolean isSuccessful;
    private Map<String, TestResultQuestion> questions;

    public TestResult() {

    }

    public TestResult(Test test, String uid, List<Question> questions) {
        this.testId = test.getId();
        this.userId = uid;
        this.passingDate = DateService.fromCalendar(new GregorianCalendar());
        this.questions = new HashMap<>();

        for (int i = 0; i < questions.size(); i++) {
            this.questions.put(i + "_key", new TestResultQuestion(questions.get(i)));
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassingDate() {
        return passingDate;
    }

    public void setPassingDate(String passingDate) {
        this.passingDate = passingDate;
    }

    public float getTotalScores() {
        return totalScores;
    }

    public void setTotalScores(float totalScores) {
        this.totalScores = totalScores;
    }

    public Map<String, TestResultQuestion> getQuestions() {
        return questions;
    }

    public List<TestResultQuestion> questionsToArray() {
        List<TestResultQuestion> testResultQuestions = new ArrayList<>(questions.values());
        testResultQuestions.sort(Comparator.comparingInt(TestResult.TestResultQuestion::getOrder));
        return testResultQuestions;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public static class TestResultQuestion {
        private String text;
        private float score;
        private int order;
        private QuestionType type;
        private Map<String, Answer> answers;

        public TestResultQuestion() {

        }

        public TestResultQuestion(Question question) {
            text = question.getText();
            order = question.getOrder();
            score = 0;
            answers = new HashMap<>();
            type = question.getType();
            int i = 0;

            for (Map.Entry<String, Answer> entry : question.getAnswers().entrySet()) {
                if (question.getType() == QuestionType.RADIO && i == 0) {
                    answers.put(entry.getKey(), new Answer(entry.getValue().getText(), true));
                } else {
                    answers.put(entry.getKey(), new Answer(entry.getValue().getText(), false));
                }

                i++;
            }
        }

        public float getScore() {
            return score;
        }

        public void setScore(float score) {
            this.score = score;
        }

        public Map<String, Answer> getAnswers() {
            return answers;
        }

        public void setAnswers(Map<String, Answer> answers) {
            this.answers = answers;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public QuestionType getType() {
            return type;
        }

        public void setType(QuestionType type) {
            this.type = type;
        }
    }
}
