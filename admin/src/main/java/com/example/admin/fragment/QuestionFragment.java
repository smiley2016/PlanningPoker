package com.example.admin.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin.R;
import com.example.admin.adapter.QuestionAdapter;
import com.example.admin.service.FireBaseDataManager;
import com.example.common.Question;
import com.example.admin.util.FragmentNavigation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuestionFragment extends BaseFragment {
    private static final String TAG = QuestionFragment.class.getName();

    @BindView(R.id.create_question_button)
    Button createQuestionButton;

    @BindView(R.id.question_recycler_view)
    RecyclerView recyclerView;

    private QuestionAdapter adapter;
    private long sessionId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.question_fragment, container, false);
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
        createQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putLong("SESSION_ID", sessionId);
                FragmentNavigation.getInstance(rootView.getContext()).showCreateQuestionFragment(bundle);
            }
        });

        adapter = new QuestionAdapter();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        recyclerView.setAdapter(adapter);

        FireBaseDataManager.getInstance().getQuestions(sessionId, adapter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getArguments() != null) {
            sessionId = getArguments().getLong("SESSION_ID");
        }
    }
}
