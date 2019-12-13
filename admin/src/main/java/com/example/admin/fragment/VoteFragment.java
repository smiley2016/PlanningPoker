package com.example.admin.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin.R;
import com.example.admin.adapter.VoteAdapter;
import com.example.admin.service.FireBaseDataManager;
import com.example.admin.service.OnVoteCardGettedListener;
import com.example.admin.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VoteFragment extends BaseFragment implements OnVoteCardGettedListener {

    @BindView(R.id.vote_recycler_view)
    RecyclerView recyclerView;

    private long sessionId;
    private long questionId;

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
        FireBaseDataManager.getInstance().getVoteCards(sessionId, cards, this);
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
        }
    }

    @Override
    public void onVoteCardGet(long cardIndex, ArrayList<String> cards) {
        VoteAdapter adapter = new VoteAdapter(createArrayFromString(cards.get((int) cardIndex)), sessionId, questionId);

        recyclerView.setLayoutManager(new GridLayoutManager(rootView.getContext(), 3));
        recyclerView.setAdapter(adapter);
    }
}
