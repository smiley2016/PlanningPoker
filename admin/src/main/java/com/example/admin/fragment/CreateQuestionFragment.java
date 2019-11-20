package com.example.admin.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.admin.R;
import com.example.admin.util.FragmentNavigation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateQuestionFragment extends BaseFragment {

    private static final String TAG = CreateQuestionFragment.class.getName();

    @BindView(R.id.question_created_start_button)
    Button questionCreateButton;

    @BindView(R.id.story_edit_text)
    EditText storyEditText;

    @BindView(R.id.description_edit_text)
    EditText descriptionEditText;

    @BindView(R.id.session_id_text_view)
    TextView sessionIdTextView;

    private int sessionId, maxAnswerId;
    private String story, description;
    private FirebaseFirestore db;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getArguments() != null) {
            int sessionId = getArguments().getInt("SESSION_ID", -1);
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
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

        if(sessionId != -1){
            sessionIdTextView.setText(sessionId);
        }

        questionCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                story = storyEditText.getText().toString();
                description = descriptionEditText.getText().toString();
                final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


                db.collection("Question")
                        .orderBy("AnswerId")
                        .limit(1)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful() && task.getResult() != null)
                                {
                                    for (QueryDocumentSnapshot snapshot: task.getResult()){
                                        maxAnswerId = (int) snapshot.getData().get("AnswerId");
                                    }

                                    Map<String, Object> question = new HashMap<>();
                                    question.put("AnswerId", (maxAnswerId+1));
                                    question.put("SessionId", sessionId);
                                    question.put("Story", story);
                                    question.put("Description", description);
                                    if (currentUser != null) {
                                        question.put("UID", currentUser.getUid());
                                    }else{
                                        Log.e(TAG, "User is null");
                                        return;
                                    }

                                    db.collection("Question")
                                            .add(question)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    FragmentNavigation.getInstance(rootView.getContext())
                                                            .showStatisticsFragment();
                                                }
                                            });
                                }
                            }
                        });
            }
        });
    }
}
