package com.example.planningpoker.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.Question;
import com.example.planningpoker.R;
import com.example.planningpoker.util.FragmentNavigation;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    private Context context;
    private ArrayList<Question> questionList;
    private long uid;

    public QuestionAdapter(long id) {
        uid = id;
        this.questionList = new ArrayList<>();
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new QuestionViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.question_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        holder.bind(questionList.get(position));
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public void addToList(Question question) {
        questionList.add(question);
        notifyDataSetChanged();
    }

    class QuestionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.question_text_view)
        TextView questionTextView;

        @BindView(R.id.question_story)
        TextView questionStory;

        @BindView(R.id.question_item_layout)
        ConstraintLayout parentLayout;

        QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(final Question question) {
            questionTextView.setText(question.getQuestion());
            questionStory.setText(question.getStory());

            parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putLong("SESSION_ID", question.getSessionId());
                    bundle.putLong("QUESTION_ID", question.getQuestionId());
                    bundle.putLong("UID", uid);
                    FragmentNavigation.getInstance(context).showVoteFragment(bundle);
                }
            });
        }
    }
}
