package com.example.admin.service;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.admin.R;
import com.example.admin.interfaces.ForgotDataFragmentCallback;
import com.example.admin.util.Dialogs;
import com.example.admin.util.FragmentNavigation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class FireBaseDataManager {
    private static final String TAG = FireBaseDataManager.class.getName();
    private static FireBaseDataManager sInstance;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String uid;
    private FirebaseUser user;
    private String email;
    private String password;
    private String document;

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

    public void loginUser(final Context context, final EditText emailText, final EditText passwordText, final AlertDialog dialog) {
        email = emailText.getText().toString().trim();
        password = passwordText.getText().toString().trim();

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
            emailText.requestFocus();
            emailText.setError(emailText.getContext().getString(R.string.not_valid_email));
            return;
        }

        uid = null;


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = mAuth.getCurrentUser();
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
                                                            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                                    if (task.isSuccessful()) {
                                                                        dialog.dismiss();
                                                                        FragmentNavigation.getInstance(context).showHomeFragment();
                                                                    }
                                                                }
                                                            });
                                                        } else {
                                                            dialog.dismiss();
                                                            Dialogs.showAlertDialog(context,
                                                                    emailText,
                                                                    passwordText,
                                                                    context.getString(R.string.admin_privileges),
                                                                    context.getString(R.string.no_admin_privileges_message),
                                                                     dialog);
                                                        }
                                                    }
                                                }
                                            }
                                        });
                            }

                        } else {
                            if (task.getException() != null && task.getException() instanceof FirebaseAuthInvalidUserException) {
                                Log.e(TAG, task.getException().toString());
                                dialog.dismiss();
                                Dialogs.showAlertDialog(context,
                                        emailText,
                                        passwordText,
                                        context.getString(R.string.user_not_exist),
                                        context.getString(R.string.the_user_doesnt_exist_in_database),
                                        dialog);
                            }else if(task.getException() != null && task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                                dialog.dismiss();
                                Dialogs.showAlertDialog(context,
                                        emailText,
                                        passwordText,
                                        context.getString(R.string.wrong_password),
                                        context.getString(R.string.wrong_password_message),
                                        dialog);
                            }

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

    public void deleteSession(long sessionId, final Context context){

        searchSessionsBySessionUID(sessionId, context);

        searchSessionMembersBySessionId(sessionId);

        searchQuestionInSessions(sessionId);

        searchAnswerBySessionId(sessionId);

    }

    private void searchAnswerBySessionId(long sessionId){
        db.collection("Answer")
                .whereEqualTo("SessionId", sessionId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && task.getResult() != null){
                            for(QueryDocumentSnapshot snapshot: task.getResult()){
                                deleteAnswer(snapshot);
                            }
                        }
                    }
                });
    }

    private void deleteAnswer(QueryDocumentSnapshot snapshot){
        db.collection("Answer")
                .document(snapshot.getId())
                .delete();
    }

    private void deleteQuestionsFromSession(QueryDocumentSnapshot snapshot){
        db.collection("Question")
                .document(snapshot.getId())
                .delete();
    }

    private void searchQuestionInSessions(final long sessionId){
        db.collection("Question")
                .whereEqualTo("SessionId", sessionId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && task.getResult()!= null){
                            for(QueryDocumentSnapshot snapshot: task.getResult()){
                                deleteQuestionsFromSession(snapshot);
                            }
                        }
                    }
                });
    }

    private void searchSessionsBySessionUID(final long sessionId, final Context context){
        db.collection("Session")
                .whereEqualTo("SessionId", sessionId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && task.getResult() != null){
                            for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                deleteSession(documentSnapshot.getId(), context);
                            }
                        }
                    }
                });
    }

    private void deleteSession(String document, final Context context){
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

    private void deleteUserFromSession(long id){
        db.collection("User")
                .whereEqualTo("id", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && task.getResult() != null){
                            for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                String doc = documentSnapshot.getId();
                                db.collection("User")
                                        .document(doc)
                                        .delete();
                            }
                        }
                    }
                });
    }

    private void searchSessionMembersBySessionId(long sessionId){
        db.collection("SessionMembers")
                .whereEqualTo("SessionId", sessionId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && task.getResult() != null){
                            for(QueryDocumentSnapshot snapshot: task.getResult()){
                                deleteSessionMembersByUID((long)snapshot.getData().get("UID"), snapshot.getId());
                            }
                        }
                    }
                });
    }

    private void deleteSessionMembersByUID(final long id, String document){
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

}
