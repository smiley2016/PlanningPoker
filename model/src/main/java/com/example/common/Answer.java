package com.example.common;

public class Answer {
    private long uid;
    private long sessionId;
    private long questionId;
    private String answer;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public Answer(long uid, long sessionId, long questionId, String answer) {
        this.uid = uid;
        this.sessionId = sessionId;
        this.questionId = questionId;
        this.answer = answer;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }
}
