package com.example.admin.service;

import android.app.AlertDialog;
import android.app.Dialog;

public interface OnLoginCredentialsListener {
    void onPasswordIncorrect(AlertDialog dialog, String email, String password);

    void onNoAdminPermission(AlertDialog dialog, String email, String password);

    void onUserNotExist(AlertDialog dialog, String email, String password);

    void onNotValidEmail();
}
