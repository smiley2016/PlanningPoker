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

public class HomeFragment extends BaseFragment{

    private static final String TAG = HomeFragment.class.getName();
    private RecyclerView sessionRecyclerView;

    private FirebaseFirestore db;
    private long members;
    private SessionAdapter adapter;
    private ArrayList<Session> sessions;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessions = new ArrayList<>();

//        db = FirebaseFirestore.getInstance();
//        db.collection("Session")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if(task.isSuccessful() && task.getResult() != null){
//                            for(final QueryDocumentSnapshot snapshot: task.getResult()){
//                                long sessionId = (long) snapshot.getData().get("SessionId");
//                                db.collection("Members")
//                                        .whereEqualTo("SessionId", sessionId)
//                                        .get()
//                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<QuerySnapshot> memberTask) {
//                                                if(memberTask.isSuccessful() && memberTask.getResult() != null){
//                                                    members = (long) memberTask.getResult().size();
//                                                    sessions.add(new Session(members,
//                                                            (String) snapshot.getData().get("SessionName"),
//                                                            (String)snapshot.getData().get("Time"),
//                                                            (String) snapshot.getData().get("EndTime"),
//                                                            (boolean)snapshot.getData().get("IsPrivate"),
//                                                            (long) snapshot.getData().get("SessionId")));
//                                                } else {
//                                                    Log.e(TAG, memberTask.getException().toString());
//                                                }
//                                                Log.e(TAG, String.valueOf(sessions.size()));
//                                            }
//                                        });
//                            }
//
//                        }
//                        adapter.notifyDataSetChanged();
//
//                    }
//
//                });

        Log.e(TAG, "onCreate: " );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if(rootView == null){
            rootView = inflater.inflate(R.layout.home_fragment, container, false);

        }
        Log.e(TAG, "onCreateView: ");
        return rootView;
    }

    private void initViews() {

        sessions.add(new Session(6,
                "Szar",
                "00:00",
                "24:00",
                false,
                1));

        adapter = new SessionAdapter(sessions, rootView.getContext());

        sessionRecyclerView = rootView.findViewById(R.id.home_recycler_view);
        sessionRecyclerView.setAdapter(adapter);
        sessionRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        Log.e(TAG, "onViewCreated");
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
