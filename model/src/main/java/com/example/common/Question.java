package com.example.common;

public class Question {
    private long questionId;
    private long sessionId;
    private String question;
    private String story;

    public Question(long questionId, long sessionId, String question, String story) {
        this.questionId = questionId;
        this.sessionId = sessionId;
        this.question = question;
        this.story = story;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }
}
