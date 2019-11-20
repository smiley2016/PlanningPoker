package com.example.admin.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin.R;
import com.example.admin.adapter.SessionAdapter;
import com.example.common.Session;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends BaseFragment{

    @BindView(R.id.statistics_recycler_view)
    RecyclerView statisticsRecyclerView;

    private FirebaseFirestore db;
    private Long members;
    private SessionAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.home_fragment, container, false);
        }
        ButterKnife.bind(this, rootView);
        initViews();
        return rootView;
    }

    private void initViews() {
        final ArrayList<Session> sessions = new ArrayList<>();

        db.collection("Session")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && task.getResult() != null){
                            adapter = new SessionAdapter(sessions);
                            statisticsRecyclerView.setHasFixedSize(true);
                            statisticsRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
                            statisticsRecyclerView.setAdapter(adapter);
                            for(QueryDocumentSnapshot snapshot: task.getResult()){
                                db.collection("Members")
                                        .whereEqualTo("SessionId", snapshot.getData().get("SessionId"))
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful() && task.getResult() != null){
                                                    if(task.getResult().size() == 0){
                                                        members = 0L;
                                                    }else{
                                                        members = (long) task.getResult().size();
                                                    }
                                                }
                                            }
                                        });
                                sessions.add(
                                        new Session(members,
                                                (String) snapshot.getData().get("SessionName"),
                                                (String)snapshot.getData().get("Time"),
                                                (String) snapshot.getData().get("EndTime"),
                                                (boolean)snapshot.getData().get("IsPrivate"),
                                                (long)snapshot.getData().get("SessionId"))
                                );
                                adapter.notifyDataSetChanged();
                            }

                        }

                    }
                });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        adapter.getTimerHandler().removeCallbacks(adapter.getTimerRunnable());
    }
}
