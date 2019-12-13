package com.example.admin.service;

import java.util.ArrayList;

public interface OnVoteCardGettedListener {
    void onVoteCardGet(long cardIndex, ArrayList<String> cards);
}
