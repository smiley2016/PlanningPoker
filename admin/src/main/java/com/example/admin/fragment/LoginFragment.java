package com.example.admin.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
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
import com.example.admin.service.OnLoginCredentialsListener;
import com.example.admin.util.Dialogs;
import com.example.admin.util.FragmentNavigation;
import com.example.admin.util.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

public class LoginFragment extends BaseFragment implements OnLoginCredentialsListener {

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
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                FireBaseDataManager.getInstance().loginUser(rootView.getContext(), email, password, dialog, LoginFragment.this);
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

    @Override
    public void onPasswordIncorrect(AlertDialog dialog, String email, String password) {

        dialog.dismiss();
        Dialogs.showAlertDialog(rootView.getContext(),
                email,
                password,
                getResources().getString(R.string.wrong_password),
                getResources().getString(R.string.wrong_password_message),
                dialog, this);
    }

    @Override
    public void onNoAdminPermission(AlertDialog dialog, String email, String password) {
        dialog.dismiss();
        Dialogs.showAlertDialog(rootView.getContext(),
                email,
                password,
                getResources().getString(R.string.admin_privileges),
                getResources().getString(R.string.no_admin_privileges_message),
                dialog, this);

    }

    @Override
    public void onUserNotExist(AlertDialog dialog, String email, String password) {
        Dialogs.showAlertDialog(rootView.getContext(),
                email,
                password,
                getResources().getString(R.string.user_not_exist),
                getResources().getString(R.string.the_user_doesnt_exist_in_database),
                dialog,this);
    }

    @Override
    public void onNotValidEmail() {
        emailEditText.requestFocus();
        emailEditText.setError("Not a valid email!");
    }
}
