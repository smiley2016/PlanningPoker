package com.example.admin.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import com.example.admin.service.FireBaseDataManager;
import com.example.admin.service.OnLoginCredentialsListener;

public class Dialogs {

    public static void showAlertDialog(final Context context,
                                       final String emailText,
                                       final String passwordText,
                                       String title,
                                       String message,
                                       final AlertDialog aDialog,
                                       final OnLoginCredentialsListener listener){
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("TRY AGAIN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        FireBaseDataManager.getInstance().loginUser(context, emailText, passwordText, aDialog, listener);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
