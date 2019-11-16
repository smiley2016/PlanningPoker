package com.example.admin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.admin.R;
import com.example.admin.interfaces.ForgotDataFragmentCallback;
import com.example.admin.service.FireBaseDataManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgotDataFragment extends BaseFragment implements ForgotDataFragmentCallback {

    @BindView(R.id.recover_email_editText)
    EditText recoverEmailEditText;

    @BindView(R.id.recover_data_button)
    Button recoverSendButton;

    @BindView(R.id.feedback_text_view)
    TextView feedBackTextView;

    @BindView(R.id.forgot_data_message_text_view)
    TextView forgotPasswordTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_forgot_data, container, false);
            ButterKnife.bind(this, rootView);
        }
        initViews();
        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void initViews() {
        recoverSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireBaseDataManager.getInstance().recoverData(recoverEmailEditText, ForgotDataFragment.this);
            }
        });
    }

    @Override
    public void setVisibility(int visibility, boolean isTaskSuccess) {
        if(isTaskSuccess){
            recoverSendButton.setVisibility(visibility);
            recoverEmailEditText.setVisibility(visibility);
            forgotPasswordTextView.setText(R.string.data_sent_to_your_email);

        }else{
            feedBackTextView.setVisibility(visibility);
        }
    }

    @Override
    public void sendMail(Intent intent) {
        startActivity(Intent.createChooser(intent, "Send email..."));
    }
}
