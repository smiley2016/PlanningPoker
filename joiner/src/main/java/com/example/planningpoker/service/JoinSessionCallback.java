package com.example.planningpoker.service;

public interface JoinSessionCallback {
    void onSessionJoined(long id);
    void onSessionNotExist();
}
