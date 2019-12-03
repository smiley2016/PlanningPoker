package com.example.admin.fragment;

import android.os.Bundle;
import android.util.Log;
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

public class HomeFragment extends BaseFragment {

    private static final String TAG = HomeFragment.class.getName();

    private FirebaseFirestore db;
    private long members;
    private SessionAdapter adapter;
    private ArrayList<Session> sessions;
    private String story;
    private QueryDocumentSnapshot snapshot;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessions = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        db.collection("Session")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (final QueryDocumentSnapshot snapshot : task.getResult()){
                                final long sessionId = (long) snapshot.getData().get("SessionId");
                                db.collection("SessionMembers")
                                        .whereEqualTo("SessionId", sessionId)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> smTask) {
                                                if (smTask.isSuccessful() && smTask.getResult() != null) {
                                                    members = (long) smTask.getResult().size();
                                                    addToList(snapshot, members);
                                                } else {
                                                    if (smTask.getException() != null)
                                                        Log.e(TAG, smTask.getException().toString());
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.home_fragment, container, false);

        }
        Log.e(TAG, "onCreateView: ");
        initViews();
        return rootView;
    }

    private void addToList(QueryDocumentSnapshot snapshot, long members){
        adapter.addToList(new Session(this.members,
                (String) snapshot.getData().get("SessionName"),
                (String) snapshot.getData().get("Time"),
                (String) snapshot.getData().get("EndTime"),
                (boolean) snapshot.getData().get("IsPrivate"),
                (long) snapshot.getData().get("SessionId"),
                story,
                (long) snapshot.getData().get("IndexOfCard")));
    }

    private void initViews() {

        adapter = new SessionAdapter(sessions, rootView.getContext());

        RecyclerView sessionRecyclerView = rootView.findViewById(R.id.home_recycler_view);
        sessionRecyclerView.setAdapter(adapter);
        sessionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated");
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
