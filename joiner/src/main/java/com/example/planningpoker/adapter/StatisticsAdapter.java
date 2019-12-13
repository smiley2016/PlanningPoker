package com.example.planningpoker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.Answer;
import com.example.planningpoker.R;
import com.example.planningpoker.service.FirebaseDataManager;
import com.example.planningpoker.service.OnStatisticsCallback;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.StatisticsViewHolder> {

    private ArrayList<Answer> answers;

    public StatisticsAdapter() {
        this.answers = new ArrayList<>();
    }

    @NonNull
    @Override
    public StatisticsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StatisticsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.statistics_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StatisticsViewHolder holder, int position) {
        Answer answer = answers.get(position);
        holder.bind(answer);
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    public void addToList(Answer answer) {
        answers.add(answer);
        notifyDataSetChanged();
    }

    class StatisticsViewHolder extends RecyclerView.ViewHolder implements OnStatisticsCallback {
        @BindView(R.id.userName_text_view)
        TextView userNameTextView;

        @BindView(R.id.vote_text_view)
        TextView voteTextView;

        private String name;
        private String uAnswer;

        StatisticsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Answer answer) {
            uAnswer = answer.getAnswer();
            FirebaseDataManager.getsInstance().getUserName(answer.getUid(), this);

        }

        @Override
        public void onUserNameUpdate(String name) {
            userNameTextView.setText(name);
            voteTextView.setText(uAnswer);
        }
    }
}
