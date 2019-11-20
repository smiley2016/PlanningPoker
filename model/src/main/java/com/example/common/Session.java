package com.example.common;

public class Session {
    private long members;
    private String sessionName;
    private String time;
    private String endTimer;
    private boolean isPrivate;
    private Long sessionId;

    public Session(long members, String sessionName, String time, String endTimer, boolean isPrivate, Long sessionId) {
        this.members = members;
        this.sessionName = sessionName;
        this.time = time;
        this.endTimer = endTimer;
        this.isPrivate = isPrivate;
        this.sessionId = sessionId;

    }

    public long getMembers() {
        return members;
    }

    public void setMembers(long members) {
        this.members = members;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public String getEndTimer() {
        return endTimer;
    }

    public void setEndTimer(String endTimer) {
        this.endTimer = endTimer;
    }
}
