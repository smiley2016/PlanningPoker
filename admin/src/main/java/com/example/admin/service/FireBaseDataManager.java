package com.example.admin.service;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.admin.R;
import com.example.admin.interfaces.ForgotDataFragmentCallback;
import com.example.admin.util.Dialogs;
import com.example.admin.util.FragmentNavigation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.StringValue;

import java.util.Objects;

public class FireBaseDataManager {
    private static final String TAG = FireBaseDataManager.class.getName();
    private static FireBaseDataManager sInstance;
    private FirebaseFirestore db;

    public static FireBaseDataManager getInstance() {
        if (sInstance == null) {
            sInstance = new FireBaseDataManager();
        }
        return sInstance;
    }

    private FireBaseDataManager() {
        db = FirebaseFirestore.getInstance();
    }

    public void loginUser(final Context context, final EditText emailText, final EditText passwordText) {
        final String email = emailText.getText().toString().trim();
        final String password = passwordText.getText().toString().trim();

        if (isValidEmail(email)) {
            emailText.requestFocus();
            emailText.setError(emailText.getContext().getString(R.string.not_valid_email));
            return;
        }


        db.collection("User")
                .whereEqualTo("Email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null && task.getResult().size() != 0) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                if (Objects.equals(snapshot.getData().get("Password"), password)) {
                                    boolean isAdmin = (boolean)snapshot.getData().get("IsAdmin");
                                    if(isAdmin){
                                        FragmentNavigation.getInstance(context).showCreateSessionFragment();
                                    }else{
                                        Dialogs.showAlertDialog(context,
                                                emailText,
                                                passwordText,
                                                context.getString(R.string.admin_privileges),
                                                context.getString(R.string.no_admin_privileges_message));
                                    }
                                } else {
                                    Dialogs.showAlertDialog(context,
                                            emailText,
                                            passwordText,
                                            context.getString(R.string.wrong_password),
                                            context.getString(R.string.wrong_password_message));
                                }
                            }
                        } else {
                            if (task.getResult() != null && task.getResult().isEmpty()) {
                                Dialogs.showAlertDialog(context,
                                        emailText,
                                        passwordText,
                                        context.getString(R.string.user_not_exist),
                                        context.getString(R.string.the_user_doesnt_exist_in_database));
                            }
                        }
                    }
                });
    }

    private static boolean isValidEmail(CharSequence target) {
        return (TextUtils.isEmpty(target) || !Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public void recoverData(EditText recoverEmailEditText, final ForgotDataFragmentCallback callback) {
        String recoveryEmail = recoverEmailEditText.getText().toString().trim();

        if (isValidEmail(recoveryEmail)) {
            recoverEmailEditText.requestFocus();
            recoverEmailEditText.setError(recoverEmailEditText.getContext().getString(R.string.not_valid_email));
            return;
        }

        db.collection("User")
                .whereEqualTo("Email", recoveryEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && task.getResult() != null && task.getResult().size() != 0){
                            for(QueryDocumentSnapshot snapshot: task.getResult()){
                                sendEmailWithData(snapshot, callback);
                            }
                        }else{
                            callback.setVisibility(View.VISIBLE, false);
                        }
                    }
                });
    }

    private void sendEmailWithData(QueryDocumentSnapshot snapshot, ForgotDataFragmentCallback callback){
        callback.setVisibility(View.GONE, true);

    }
}
