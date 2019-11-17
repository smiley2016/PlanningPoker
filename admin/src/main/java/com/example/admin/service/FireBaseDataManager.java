package com.example.admin.service;

import android.content.Context;
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
import com.example.common.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class FireBaseDataManager {
    private static final String TAG = FireBaseDataManager.class.getName();
    private static FireBaseDataManager sInstance;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String uid = null;

    public static FireBaseDataManager getInstance() {
        if (sInstance == null) {
            sInstance = new FireBaseDataManager();
        }
        return sInstance;
    }

    private FireBaseDataManager() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    public void loginUser(final Context context, final EditText emailText, final EditText passwordText) {
        final String email = emailText.getText().toString().trim();
        final String password = passwordText.getText().toString().trim();

        if (isValidEmail(email)) {
            emailText.requestFocus();
            emailText.setError(emailText.getContext().getString(R.string.not_valid_email));
            return;
        }



        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        myRef.equalTo(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot != null && snapshot.getValue(User.class) != null) {
                        if (snapshot.getValue(User.class).getEmail().equals(email)) {
                            uid = Objects.requireNonNull(snapshot.getValue(User.class)).getUid();
                        }
                    }
                }

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
                                                        FragmentNavigation.getInstance(context).showCreateSessionFragment();
                                                    } else {
                                                        Dialogs.showAlertDialog(context,
                                                                emailText,
                                                                passwordText,
                                                                context.getString(R.string.wrong_password),
                                                                context.getString(R.string.wrong_password_message));
                                                    }
                                                }
                                            });
                                        } else {
                                            Dialogs.showAlertDialog(context,
                                                    emailText,
                                                    passwordText,
                                                    context.getString(R.string.admin_privileges),
                                                    context.getString(R.string.no_admin_privileges_message));
                                        }
                                    }
                                } else {
                                    Dialogs.showAlertDialog(context,
                                            emailText,
                                            passwordText,
                                            context.getString(R.string.user_not_exist),
                                            context.getString(R.string.the_user_doesnt_exist_in_database));
                                }
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, databaseError.toException().toString());
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
                        if (task.getResult().getSignInMethods().size() == 0) {
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

}
