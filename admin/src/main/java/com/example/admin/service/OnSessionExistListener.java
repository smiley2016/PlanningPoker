package com.example.admin.service;

import com.google.firebase.firestore.QueryDocumentSnapshot;

public interface OnSessionExistListener {
    void onNoItemInList();
    void onSessionExist(QueryDocumentSnapshot snapshot, long members);
}
