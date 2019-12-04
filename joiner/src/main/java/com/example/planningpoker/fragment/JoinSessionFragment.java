package com.example.planningpoker.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.planningpoker.R;
import com.example.planningpoker.service.FirebaseDataManager;
import com.example.planningpoker.service.JoinSessionCallback;
import com.example.planningpoker.util.FragmentNavigation;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JoinSessionFragment extends BaseFragment implements JoinSessionCallback {

    private static final String TAG = JoinSessionFragment.class.getName();
    private String sessionId;
    private String name;

    @BindView(R.id.join_button)
    Button joinButton;

    @BindView(R.id.session_id_edit_text)
    EditText sessionIdEditText;

    @BindView(R.id.name_edit_text)
    EditText nameEditText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.join_session_fragment, container, false);
        }
        ButterKnife.bind(this, rootView);
        initViews();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initViews() {
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionId = sessionIdEditText.getText().toString().trim();
                name = nameEditText.getText().toString().trim();
                FirebaseDataManager.getsInstance().joinToSession(name, Long.valueOf(sessionId), JoinSessionFragment.this);
            }
        });
    }

    @Override
    public void onSessionJoined() {
        Bundle bundle = new Bundle();
        bundle.putString("JOINER_NAME", name);
        FragmentNavigation.getInstance(rootView.getContext())
                .showQuestionFragment(bundle);
    }

    @Override
    public void onSessionNotExist() {
        Toast.makeText(rootView.getContext(), "The session isn't exist in database" , Toast.LENGTH_SHORT).show();
    }
}
