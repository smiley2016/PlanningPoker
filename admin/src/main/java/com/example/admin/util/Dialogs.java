package com.example.admin.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import com.example.admin.service.FireBaseDataManager;
import com.example.admin.service.OnProgressBarVisibilityListener;

public class Dialogs {

    public static void showAlertDialog(final Context context,
                                       final EditText emailText,
                                       final EditText passwordText,
                                       String title,
                                       String message,
                                       final OnProgressBarVisibilityListener listener,
                                       final AlertDialog aDialog){
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("TRY AGAIN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        FireBaseDataManager.getInstance(listener).loginUser(context, emailText, passwordText, aDialog);
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
