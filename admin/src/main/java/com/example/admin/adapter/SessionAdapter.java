package com.example.admin.adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
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

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.AdapterViewHolder> {

    private ArrayList<Session> sessions;
    private Context context;
    private Handler timerHandler;
    private Runnable timerRunnable;

    public SessionAdapter(ArrayList<Session> sessions) {
        this.sessions = sessions;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new AdapterViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.session_element, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        Session session = sessions.get(position);
        holder.bind(session);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public Handler getTimerHandler() {
        return timerHandler;
    }

    public Runnable getTimerRunnable() {
        return timerRunnable;
    }

    class AdapterViewHolder extends RecyclerView.ViewHolder{

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


        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final Session session) {
            membersTextView.setText(String.valueOf(session.getMembers()));
            sessionStory.setText(String.valueOf(session.getSessionName()));
            timerTextView.setText(String.valueOf(session.getTime()));

            timerHandler = new Handler();
            timerRunnable = new Runnable() {
                @Override
                public void run() {
                    long millis = System.currentTimeMillis();
                    int seconds = (int) (millis / 1000);
                    int minutes = seconds / 60;
                    seconds = seconds % 60;

                    timerTextView.setText(String.format("%d:%02d", minutes, seconds));

                    timerHandler.postDelayed(this, 500);
                }
            };

            timerHandler.postDelayed(timerRunnable, 0);
        }
    }
}
