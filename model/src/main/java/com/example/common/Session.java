package com.example.common;

public class Session {
    private long members;
    private String sessionName;
    private String time;
    private String endTimer;
    private boolean isPrivate;
    private long sessionId;
    private String story;
    private long indexOfCard;

    public Session(long members, String sessionName, String time, String endTimer, boolean isPrivate, long sessionId, String story, long indexOfCard) {
        this.members = members;
        this.sessionName = sessionName;
        this.time = time;
        this.endTimer = endTimer;
        this.isPrivate = isPrivate;
        this.sessionId = sessionId;
        this.story = story;
        this.indexOfCard = indexOfCard;
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

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public String getEndTimer() {
        return endTimer;
    }

    public void setEndTimer(String endTimer) {
        this.endTimer = endTimer;
    }

    public String getSessionDescription() {return story;}

    public void setSessionDescription(String sessionDescription) {
        this.story = sessionDescription;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public long getIndexOfCard() {
        return indexOfCard;
    }

    public void setIndexOfCard(long indexOfCard) {
        this.indexOfCard = indexOfCard;
    }
}
