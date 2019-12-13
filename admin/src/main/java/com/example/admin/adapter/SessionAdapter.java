package com.example.admin.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.admin.R;
import com.example.admin.service.FireBaseDataManager;
import com.example.admin.service.NoItemInListCallback;
import com.example.admin.util.FragmentNavigation;
import com.example.common.Session;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.Holder> {
    private static final String TAG = SessionAdapter.class.getName();
    private ArrayList<Session> sessions;
    private Context context;
    private NoItemInListCallback callback;

    public SessionAdapter(ArrayList<Session> sessions, Context context, NoItemInListCallback callback) {
        this.sessions = sessions;
        this.context = context;
        this.callback = callback;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.session_element, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Session session = sessions.get(position);
        holder.bind(session);

        Log.e(TAG, "onBindViewHolder: " + sessions.size());
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }

    public void addToList(Session session){
        sessions.add(session);
        notifyDataSetChanged();
    }

    private void wipeList(){
        sessions.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.private_image_view)
        ImageView privateImageView;

        @BindView(R.id.session_story_text_view)
        TextView sessionStory;

        @BindView(R.id.members_text_view)
        TextView membersTextView;

        @BindView(R.id.open_session_button)
        Button openSessionButton;

        @BindView(R.id.join_session_button)
        Button joinSessionButton;

        @BindView(R.id.delete_session_button)
        ImageView deleteSessionButton;

        Holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(final Session session) {

            final Bundle bundle = new Bundle();
//            bundle.putLong("SESSION_MEMBER", session.getMembers());
//            bundle.putString("SESSION_STORY", session.getSessionName());
//            bundle.putLong("SESSION_CARD_INDEX", session.getIndexOfCard());
//            bundle.putString("SESSION_NAME", session.getSessionName());
//            bundle.putString("SESSION_START_TIME", session.getTime());
//            bundle.putString("SESSION_END_TIME", session.getEndTimer());
//            bundle.putBoolean("IS_PRIVATE", session.isPrivate());
            bundle.putLong("SESSION_ID", session.getSessionId());


            membersTextView.setText(String.valueOf(session.getMembers()));
            sessionStory.setText(session.getSessionName());

            if (session.isPrivate()) {
                Glide.with(context).load(R.drawable.ic_lock_outline_black_24dp)
                        .skipMemoryCache(true)
                        .override(75)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(privateImageView);
            } else {
                Glide.with(context).load(R.drawable.ic_lock_open_black_24dp)
                        .skipMemoryCache(true)
                        .override(75)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(privateImageView);
            }

            joinSessionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(FirebaseAuth.getInstance().getCurrentUser() != null){
                        Map<String, Object> memberUser = new HashMap<>();
                        memberUser.put("SessionId", session.getSessionId());
                        memberUser.put("UID", FirebaseAuth.getInstance().getCurrentUser().getUid());

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("SessionMembers")
                                .add(memberUser)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        FragmentNavigation.getInstance(context).showQuestionFragment(bundle);
                                        wipeList();
                                    }
                                });
                    }
                }
            });

            openSessionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    wipeList();
                    FragmentNavigation.getInstance(context).showQuestionFragment(bundle);
                }
            });

            deleteSessionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FireBaseDataManager.getInstance().deleteSession(session.getSessionId(), context);
                    sessions.remove(session);
                    notifyDataSetChanged();
                    if (sessions.size() == 0){
                        callback.onNoItemInList();
                    }
                }
            });
        }
    }
}
