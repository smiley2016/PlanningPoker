package com.example.planningpoker.service;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.common.Answer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class FirebaseDataManager {

    private static final String TAG = FirebaseDataManager.class.getName();

    private static FirebaseDataManager sInstance;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public static FirebaseDataManager getsInstance() {
        if (sInstance == null) {
            sInstance = new FirebaseDataManager();
        }
        return sInstance;
    }

    private FirebaseDataManager() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    public void joinToSession(final String name, final long sessionId, final JoinSessionCallback callback) {
        getLatestUserId(name, sessionId, callback);
    }

    private void getLatestUserId(final String name, final long sessionId, final JoinSessionCallback callback) {
        db.collection("User")
                .orderBy("id", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> idTask) {
                        if (idTask.isSuccessful() && idTask.getResult() != null) {
                            if (idTask.getResult().size() == 0) {
                                addUser(1, name, sessionId, callback);
                            }else{
                                for (QueryDocumentSnapshot snapshot: idTask.getResult()){
                                    addUser((long)snapshot.getData().get("id")+1, name, sessionId, callback);
                                }
                            }
                        }
                    }
                });
    }

    private void addUser(final long id, String name, final long sessionId, final JoinSessionCallback callback){
        HashMap<String, Object> user = new HashMap<>();
        user.put("UID", null);
        user.put("id", id);
        user.put("IsAdmin", false);
        user.put("UserName", name);

        db.collection("User")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "onSuccess: Joined");
                        addSessionMembers(id, sessionId, callback);
                    }
                });
    }

    private void addSessionMembers(final long id, long sessionId, final JoinSessionCallback callback){
        HashMap<String, Object> sessionMember = new HashMap<>();
        sessionMember.put("UID", id);
        sessionMember.put("SessionId", sessionId);
        db.collection("SessionMembers")
                .add(sessionMember)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "onSuccess: User added to sessionMember");
                        callback.onSessionJoined(id);
                    }
                });
    }

    public void getUserName(long uid, final OnStatisticsCallback callback) {
        db.collection("User")
                .whereEqualTo("id", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                callback.onUserNameUpdate((String) snapshot.getData().get("UserName"));
                            }
                        }
                    }
                });
    }

    public void getAnswer(final long sessionId, final long questionId, final OnStatisticsFragmentCallback callback) {
        db.collection("Answer")
                .whereEqualTo("SessionId", sessionId)
                .whereEqualTo("QuestionId", questionId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                callback.onAnswerGetting(new Answer(
                                        (long) snapshot.getData().get("UID"),
                                        (long) snapshot.getData().get("SessionId"),
                                        (long) snapshot.getData().get("QuestionId"),
                                        (String) snapshot.getData().get("AnswerId")));
                            }
                        }
                    }
                });
    }
}
