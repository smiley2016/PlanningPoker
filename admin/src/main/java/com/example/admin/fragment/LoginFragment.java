package com.example.admin.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.admin.R;
import com.example.admin.service.FireBaseDataManager;
import com.example.admin.service.OnProgressBarVisibilityListener;
import com.example.admin.util.FragmentNavigation;
import com.example.admin.util.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

public class LoginFragment extends BaseFragment implements OnProgressBarVisibilityListener {

    @BindView(R.id.login_email_edit_text)
    EditText emailEditText;

    @BindView(R.id.login_password_edit_text)
    EditText passwordEditText;

    @BindView(R.id.login_button)
    Button loginButton;

    @BindView(R.id.forgot_password_text_view)
    TextView forgotPasswordTextView;

    @BindView(R.id.login_progress_bar)
    ProgressBar loginProgressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_login, container, false);
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
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new SpotsDialog(rootView.getContext(), R.style.Custom);
                dialog.show();
                Utils.hideKeyboard(v);
                FireBaseDataManager.getInstance(LoginFragment.this).loginUser(rootView.getContext(), emailEditText, passwordEditText, dialog);
            }
        });

        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentNavigation.getInstance(rootView.getContext()).showForgotDataFragment();
            }
        });


    }

    @Override
    public void setVisibilityOnProgressBar(int visibility) {

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AlertDialog dialog = new SpotsDialog(rootView.getContext(), R.style.Custom);
        dialog.show();
        if (user != null) {
            FragmentNavigation.getInstance(getContext()).showHomeFragment();
            dialog.dismiss();
        }
        dialog.dismiss();
    }
}
