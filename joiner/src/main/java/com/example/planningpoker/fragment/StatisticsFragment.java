package com.example.planningpoker.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.Answer;
import com.example.planningpoker.R;
import com.example.planningpoker.adapter.StatisticsAdapter;
import com.example.planningpoker.service.FirebaseDataManager;
import com.example.planningpoker.service.OnStatisticsFragmentCallback;
import com.example.planningpoker.util.Utils;
import com.google.zxing.WriterException;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatisticsFragment extends BaseFragment implements OnStatisticsFragmentCallback {

    @BindView(R.id.qr_code_button)
    Button makeQrCodeButton;

    @BindView(R.id.statistics_recycler_view)
    RecyclerView statisticsRecyclerView;

    private Long sessionId;
    private long uid;
    private String card;
    private long questionId;
    private StatisticsAdapter adapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getArguments() != null) {
            sessionId = getArguments().getLong("SESSION_ID");
            uid = getArguments().getLong("UID");
            card = getArguments().getString("ANSWER");
            questionId = getArguments().getLong("POSITION");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.statistics_fragment, container, false);
        }
        ButterKnife.bind(this, rootView);
        initViews();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initViews() {
        adapter = new StatisticsAdapter();

        statisticsRecyclerView.setHasFixedSize(true);
        statisticsRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        statisticsRecyclerView.setAdapter(adapter);

        FirebaseDataManager.getsInstance().getAnswer(sessionId, questionId, this);

        makeQrCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = null;
                try {
                    bitmap = Utils.generateQrCode(sessionId.toString(), rootView.getContext());
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                Dialog dialog = new Dialog(rootView.getContext());
                dialog.setContentView(R.layout.qr_code_dialog);
                ImageView qrCodeImageView = dialog.findViewById(R.id.qr_code_image_view);
                qrCodeImageView.setImageBitmap(bitmap);
                dialog.show();
            }
        });
    }

    @Override
    public void onAnswerGetting(Answer answer) {
        adapter.addToList(answer);
    }
}
