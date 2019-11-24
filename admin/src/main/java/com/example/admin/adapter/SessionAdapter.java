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
import com.example.admin.util.FragmentNavigation;
import com.example.common.Session;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.Holder> {
    private static final String TAG = SessionAdapter.class.getName();
    private ArrayList<Session> sessions;
    private Context context;

    public SessionAdapter(ArrayList<Session> sessions, Context context) {
        this.sessions = sessions;
        this.context = context;
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

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.private_image_view)
        ImageView privateImageView;

        @BindView(R.id.timer)
        TextView timerTextView;

        @BindView(R.id.session_story_text_view)
        TextView sessionStory;

        @BindView(R.id.members_text_view)
        TextView membersTextView;

        @BindView(R.id.open_session_button)
        Button openSessionButton;

        @BindView(R.id.join_session_button)
        Button joinSessionButton;

        @BindView(R.id.end_timer)
        TextView endTimer;


        Holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Session session) {
            final Bundle bundle = new Bundle();
            bundle.putLong("SESSION_MEMBER", session.getMembers());
            bundle.putString("SESSION_STORY", session.getSessionName());
            bundle.putLong("SESSION_CARD_INDEX", session.getIndexOfCard());
            bundle.putString("SESSION_NAME", session.getSessionName());
            bundle.putString("SESSION_START_TIME", session.getTime());
            bundle.putString("SESSION_END_TIME", session.getEndTimer());
            bundle.putBoolean("IS_PRIVATE", session.isPrivate());
            bundle.putLong("SESSION_ID", session.getSessionId());

            membersTextView.setText(String.valueOf(session.getMembers()));
            sessionStory.setText(session.getSessionName());
            timerTextView.setText(session.getTime());
            endTimer.setText(session.getEndTimer());

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
                    FragmentNavigation.getInstance(context).showVoteFragment(bundle);
                }
            });

            openSessionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentNavigation.getInstance(context).showVoteFragment(bundle);
                }
            });
        }
    }
}
