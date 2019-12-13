package com.example.admin.fragment;

import android.content.Context;
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
import com.example.admin.service.FireBaseDataManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateQuestionFragment extends BaseFragment {

    @BindView(R.id.question_created_start_button)
    Button questionCreateButton;

    @BindView(R.id.story_edit_text)
    EditText storyEditText;

    @BindView(R.id.description_edit_text)
    EditText descriptionEditText;

    @BindView(R.id.session_id_text_view)
    TextView sessionIdTextView;

    private Long sessionId;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getArguments() != null) {
            sessionId = getArguments().getLong("SESSION_ID", -1);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.create_question_fragment, container, false);
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

        if (sessionId != -1) {
            sessionIdTextView.setText(String.valueOf(sessionId));
        }

        questionCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireBaseDataManager.getInstance().createQuestion(storyEditText.getText().toString(),
                        descriptionEditText.getText().toString(),
                        sessionId,
                        rootView.getContext());
            }
        });
    }
}
