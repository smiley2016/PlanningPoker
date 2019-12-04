package com.example.planningpoker.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planningpoker.R;
import com.example.planningpoker.adapter.VoteAdapter;
import com.example.planningpoker.util.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VoteFragment extends BaseFragment {

    @BindView(R.id.vote_recycler_view)
    RecyclerView recyclerView;

    private VoteAdapter adapter;

    private long sessionId;
    private long uid;
    private long questionId;
    private long cardIndex;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.vote_fragment, container, false);
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
        final ArrayList<String> cards = new ArrayList<>(Arrays.asList(Utils.cards));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Session")
                .whereEqualTo("SessionId", sessionId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                cardIndex = (long) snapshot.getData().get("IndexOfCard");
                                adapter = new VoteAdapter(createArrayFromString(cards.get((int) cardIndex)), sessionId, questionId, uid);

                                recyclerView.setLayoutManager(new GridLayoutManager(rootView.getContext(), 3));
                                recyclerView.setAdapter(adapter);
                            }
                        }
                    }
                });


    }

    private ArrayList<String> createArrayFromString(String cardString) {
        String[] splittedArray;
        splittedArray = cardString.split(", ");
        return new ArrayList<>(Arrays.asList(splittedArray));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getArguments() != null) {
            sessionId = getArguments().getLong("SESSION_ID");
            questionId = getArguments().getLong("QUESTION_ID");
            uid = getArguments().getLong("UID");
        }
    }
}
