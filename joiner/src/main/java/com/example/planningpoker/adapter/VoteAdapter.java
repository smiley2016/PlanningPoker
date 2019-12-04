package com.example.planningpoker.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planningpoker.R;
import com.example.planningpoker.util.FragmentNavigation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VoteAdapter extends RecyclerView.Adapter<VoteAdapter.Holder> {

    private static final String TAG = VoteAdapter.class.getName();

    private ArrayList<String> cards;
    private Context context;
    private long sessionId;
    private boolean isVoted;
    private long questionId;
    private long uid;

    public VoteAdapter(ArrayList<String> cards, long sessionId, long questionId, long uid) {
        this.cards = cards;
        this.sessionId = sessionId;
        this.questionId = questionId;
        isVoted = false;
        this.uid = uid;
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new Holder(LayoutInflater
                .from(context)
                .inflate(R.layout.vote_card_element, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        String card = cards.get(position);
        holder.bind(card, position);
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_image_view)
        ImageView cardImageView;

        @BindView(R.id.card_element_text_view)
        TextView cardTextView;

        Holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(final String card, final int position) {

            cardTextView.setText(card);


            cardImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if(!isVoted){
                        isVoted = true;
                        Log.e(TAG, "bind: " + isVoted );
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        Map<String, Object> answer;

                            answer = new HashMap<>();
                            answer.put("UID", uid);
                            answer.put("QuestionId", questionId);
                            answer.put("AnswerId", card);
                            answer.put("SessionId", sessionId);

                            db.collection("Answer")
                                    .add(answer)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(context,
                                                    "Your answer has been saved",
                                                    Toast.LENGTH_LONG).show();
                                            Bundle bundle = new Bundle();
                                            bundle.putLong("SESSION_ID", sessionId);
                                            bundle.putLong("UID", uid);
                                            bundle.putString("ANSWER", card);
                                            bundle.putInt("POSITION", position);
                                            FragmentNavigation.getInstance(context).showStatisticsFragment(bundle);
                                        }
                                    });
                    }else{
                        isVoted = false;
                        Log.e(TAG, "onClick: " + isVoted );
                    }


                }
            });

        }
    }
}
