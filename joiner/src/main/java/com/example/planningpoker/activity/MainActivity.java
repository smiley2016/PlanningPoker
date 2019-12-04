package com.example.planningpoker.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.common.Common;
import com.example.planningpoker.R;
import com.example.planningpoker.util.FragmentNavigation;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentNavigation.getInstance(this).showJoinSessionFragment();
    }
}
