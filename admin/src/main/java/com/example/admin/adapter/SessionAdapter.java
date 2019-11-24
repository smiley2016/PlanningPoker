package com.example.admin.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin.R;
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

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    static class Holder extends RecyclerView.ViewHolder {

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
            membersTextView.setText(String.valueOf(session.getMembers()));
            sessionStory.setText(session.getSessionName());
            timerTextView.setText(session.getTime());
            endTimer.setText(session.getEndTimer());

            if (session.isPrivate()) {
                privateImageView.setVisibility(View.VISIBLE);
            } else {
                privateImageView.setVisibility(View.INVISIBLE);
            }

            joinSessionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            openSessionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
