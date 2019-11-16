package com.example.admin.interfaces;

import android.content.Intent;

public interface ForgotDataFragmentCallback {
    void setVisibility(int visibility, boolean isTaskSucces);
    void sendMail(Intent intent);
}
