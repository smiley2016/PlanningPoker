package com.example.admin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.admin.R;
import com.example.admin.util.FragmentNavigation;
import com.example.common.Common;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        FragmentNavigation.getInstance(this).showLoginFragment();
    }
}
