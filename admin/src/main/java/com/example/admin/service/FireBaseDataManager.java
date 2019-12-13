package com.example.admin.service;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.admin.R;
import com.example.admin.adapter.QuestionAdapter;
import com.example.admin.interfaces.ForgotDataFragmentCallback;
import com.example.admin.util.FragmentNavigation;
import com.example.common.Question;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import dmax.dialog.SpotsDialog;

public class FireBaseDataManager {
    private static final String TAG = FireBaseDataManager.class.getName();
    private static FireBaseDataManager sInstance;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String uid;
    private FirebaseUser user;
    private String email;
    private String password;
    private Long maxSessionIdInDb;
    private Long lastQuestionId;

    public static FireBaseDataManager getInstance() {
        if (sInstance == null) {
            sInstance = new FireBaseDataManager();
        }
        return sInstance;
    }

    private FireBaseDataManager() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        uid = null;
    }

    public void loginUser(final Context context, final String emailText,
                          final String passwordText, final AlertDialog dialog,
                          final OnLoginCredentialsListener listener) {

        email = emailText;
        password = passwordText;

        switch (email) {
            case "x":
                email = "smileyb666@gmail.com";
                password = "Bartalus2019";
                break;
            case "y":
                email = "smileyb26@outlook.hu";
                password = "Bartalus2019";
                break;
            case "z":
                email = "asdas@asd.com";
                password = "asdas";
                break;
        }

        if (isValidEmail(email)) {
            listener.onNotValidEmail();
            return;
        }

        uid = null;


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = mAuth.getCurrentUser();
                            getUser(context, dialog, listener);
                        } else {
                            if (task.getException() != null && task.getException() instanceof FirebaseAuthInvalidUserException) {
                                Log.e(TAG, task.getException().toString());
                                dialog.dismiss();
                                listener.onUserNotExist(dialog, email, password);
                            } else if (task.getException() != null && task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                listener.onPasswordIncorrect(dialog, email, password);
                            }

                        }
                    }
                });
    }

    private void getUser(final Context context, final AlertDialog dialog, final OnLoginCredentialsListener listener) {
        if (user != null) {
            uid = user.getUid();
            db.collection("User")
                    .whereEqualTo("UID", uid)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null && task.getResult().size() != 0) {
                                for (QueryDocumentSnapshot querySnapshot : task.getResult()) {
                                    boolean isAdmin = (boolean) querySnapshot.getData().get("IsAdmin");
                                    if (isAdmin) {
                                        login(dialog, context);
                                    } else {
                                        listener.onNoAdminPermission(dialog, email, password);
                                    }
                                }
                            }
                        }
                    });
        }
    }


    private void login(final AlertDialog dialog, final Context context) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    dialog.dismiss();
                    FragmentNavigation.getInstance(context).showHomeFragment();
                }
            }
        });
    }

    public void getSessions(final OnSessionExistListener callback) {
        db.collection("Session")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (final QueryDocumentSnapshot snapshot : task.getResult()) {
                                getMemberNo(snapshot, callback);
                            }
                            if (task.getResult().size() == 0) {
                                callback.onNoItemInList();
                            }
                        } else {

                            Log.e(TAG, "onComplete: ");
                        }
                    }
                });
    }

    private void getMemberNo(final QueryDocumentSnapshot snapshot, final OnSessionExistListener callback) {
        final long sessionId = (long) snapshot.getData().get("SessionId");
        db.collection("SessionMembers")
                .whereEqualTo("SessionId", sessionId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> smTask) {
                        if (smTask.isSuccessful() && smTask.getResult() != null) {
                            long members = (long) smTask.getResult().size();
                            callback.onSessionExist(snapshot, members);
                        } else {
                            if (smTask.getException() != null)
                                Log.e(TAG, smTask.getException().toString());
                        }
                    }
                });
    }

    private static boolean isValidEmail(CharSequence target) {
        return (TextUtils.isEmpty(target) || !Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public void recoverData(EditText recoverEmailEditText, final ForgotDataFragmentCallback callback) {
        final String recoveryEmail = recoverEmailEditText.getText().toString().trim();

        if (isValidEmail(recoveryEmail)) {
            recoverEmailEditText.requestFocus();
            recoverEmailEditText.setError(recoverEmailEditText.getContext().getString(R.string.not_valid_email));
            return;
        }

        mAuth.fetchSignInMethodsForEmail(recoveryEmail)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (Objects.requireNonNull(
                                Objects.requireNonNull(
                                        task.getResult())
                                        .getSignInMethods())
                                .size() == 0) {
                            callback.setVisibility(View.VISIBLE, false);
                        } else {
                            sendEmailWithData(callback, recoveryEmail);
                        }
                    }
                });
    }

    private void sendEmailWithData(ForgotDataFragmentCallback callback, String recoveryEmail) {
        callback.setVisibility(View.GONE, true);
        mAuth.sendPasswordResetEmail(recoveryEmail);
    }

    public void deleteSession(long sessionId, final Context context) {

        searchSessionsBySessionUID(sessionId, context);

        searchSessionMembersBySessionId(sessionId);

        searchQuestionInSessions(sessionId);

        searchAnswerBySessionId(sessionId);

    }

    private void searchAnswerBySessionId(long sessionId) {
        db.collection("Answer")
                .whereEqualTo("SessionId", sessionId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                deleteAnswer(snapshot);
                            }
                        }
                    }
                });
    }

    private void deleteAnswer(QueryDocumentSnapshot snapshot) {
        db.collection("Answer")
                .document(snapshot.getId())
                .delete();
    }

    private void deleteQuestionsFromSession(QueryDocumentSnapshot snapshot) {
        db.collection("Question")
                .document(snapshot.getId())
                .delete();
    }

    private void searchQuestionInSessions(final long sessionId) {
        db.collection("Question")
                .whereEqualTo("SessionId", sessionId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                deleteQuestionsFromSession(snapshot);
                            }
                        }
                    }
                });
    }

    private void searchSessionsBySessionUID(final long sessionId, final Context context) {
        db.collection("Session")
                .whereEqualTo("SessionId", sessionId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                deleteSession(documentSnapshot.getId(), context);
                            }
                        }
                    }
                });
    }

    private void deleteSession(String document, final Context context) {
        db.collection("Session")
                .document(document)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Session deleted and it's all data!", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void deleteUserFromSession(long id) {
        db.collection("User")
                .whereEqualTo("id", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String doc = documentSnapshot.getId();
                                db.collection("User")
                                        .document(doc)
                                        .delete();
                            }
                        }
                    }
                });
    }

    private void searchSessionMembersBySessionId(long sessionId) {
        db.collection("SessionMembers")
                .whereEqualTo("SessionId", sessionId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                deleteSessionMembersByUID((long) snapshot.getData().get("UID"), snapshot.getId());
                            }
                        }
                    }
                });
    }

    private void deleteSessionMembersByUID(final long id, String document) {
        db.collection("SessionMembers")
                .document(document)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        deleteUserFromSession(id);
                    }
                });
    }

    public void createSession(final String sessionName,
                              final boolean isPrivate,
                              final int indexOfCard,
                              final Context context) {
        final AlertDialog dialog = new SpotsDialog(context, R.style.Custom);
        dialog.show();

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Session")
                .orderBy("SessionId", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                maxSessionIdInDb = (Long) snapshot.getData().get("SessionId");
                            }

                            if (maxSessionIdInDb == null) {
                                maxSessionIdInDb = Long.parseLong("0");
                            }

                            addSession(context,
                                    sessionName,
                                    isPrivate,
                                    indexOfCard,
                                    dialog);

                        }

                    }
                });
    }

    private void addSession(final Context context,
                            final String sessionName,
                            final boolean isPrivate,
                            final int indexOfCard,
                            final AlertDialog dialog) {
        Map<String, Object> session = new HashMap<>();
        session.put("SessionId", maxSessionIdInDb + 1);
        session.put("SessionName", sessionName);
        session.put("IsPrivate", isPrivate);
        session.put("IndexOfCard", indexOfCard);
        session.put("Time", "00:00");
        session.put("EndTime", "24:00");
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            session.put("UID", FirebaseAuth.getInstance().getCurrentUser().getUid());
        } else {
            return;
        }

        db.collection("Session")
                .add(session)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(context,
                                "DocumentSnapshot added with ID: "
                                        + documentReference.getId(),
                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        Bundle bundle = new Bundle();
                        bundle.putLong("SESSION_ID", maxSessionIdInDb + 1);
                        FragmentNavigation.getInstance(context)
                                .showHomeFragment();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                        dialog.dismiss();
                        Toast.makeText(context,
                                "Query failure: "
                                        + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void createQuestion(final String story, final String description, final long sessionId, final Context context) {
        db.collection("Question")
                .orderBy("QuestionId", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                lastQuestionId = (Long) snapshot.getData().get("QuestionId");
                            }

                            if (lastQuestionId == null) {
                                lastQuestionId = Long.parseLong("0");
                            }

                            addQuestion(sessionId, story, description, context);


                        }
                    }
                });
    }

    private void addQuestion(final long sessionId, String story, String description, final Context context) {
        Map<String, Object> question = new HashMap<>();
        question.put("SessionId", sessionId);
        question.put("Story", story);
        question.put("Description", description);
        question.put("QuestionId", lastQuestionId + 1);

        db.collection("Question")
                .add(question)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Bundle bundle = new Bundle();
                        bundle.putLong("SESSION_ID", sessionId);
                        FragmentNavigation.getInstance(context)
                                .showStatisticsFragment(bundle);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
    }

    public void getQuestions(final long sessionId, final QuestionAdapter adapter) {
        db.collection("Question")
                .whereEqualTo("SessionId", sessionId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                adapter.addToList(new Question(
                                        (long) snapshot.getData().get("QuestionId"),
                                        sessionId,
                                        (String) snapshot.getData().get("Description"),
                                        (String) snapshot.getData().get("Story")));
                            }
                        }
                    }
                });
    }

    public void getVoteCards(long sessionId, final ArrayList<String> cards, final OnVoteCardGettedListener listener) {
        db.collection("Session")
                .whereEqualTo("SessionId", sessionId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                long cardIndex = (long) snapshot.getData().get("IndexOfCard");
                                listener.onVoteCardGet(cardIndex, cards);
                            }
                        }
                    }
                });

    }
}
