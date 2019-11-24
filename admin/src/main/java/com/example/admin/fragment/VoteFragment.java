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
import com.example.admin.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VoteFragment extends BaseFragment {

    @BindView(R.id.vote_recycler_view)
    RecyclerView recyclerView;

    private VoteAdapter adapter;

    private long sessionMembers;
    private long sessionId;
    private long indexofCard;
    private String sessionName;
    private String sessionDescription;
    private String sessionStory;
    private String sessionStartTime;
    private String sessionEndTime;
    private boolean isSessionPrivate;

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
        ArrayList<String> cards = new ArrayList<>(Arrays.asList(Utils.cards));

        adapter = new VoteAdapter(createArrayFromString(cards.get((int)indexofCard)), sessionId);

        recyclerView.setLayoutManager(new GridLayoutManager(rootView.getContext(), 2));
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<String> createArrayFromString(String cardString) {
        String [] splittedArray;
        splittedArray =  cardString.split(", ");
        return new ArrayList<>(Arrays.asList(splittedArray));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getArguments() != null) {
            sessionMembers = getArguments().getLong("SESSION_MEMBER");
            indexofCard = getArguments().getLong("SESSION_CARD_INDEX");
            sessionId = getArguments().getLong("SESSION_ID");
            isSessionPrivate = getArguments().getBoolean("IS_PRIVATE");
            sessionStory = getArguments().getString("SESSION_STORY");
            sessionDescription = getArguments().getString("SESSION_DESCRIPTION");
            sessionName = getArguments().getString("SESSION_NAME");
            sessionStartTime = getArguments().getString("SESSION_START_TIME");
            sessionEndTime = getArguments().getString("SESSION_END_TIME");
        }
    }
}
