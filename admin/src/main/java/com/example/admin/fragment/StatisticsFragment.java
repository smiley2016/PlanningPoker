package com.example.admin.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin.R;
import com.example.admin.util.Utils;
import com.google.zxing.WriterException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatisticsFragment extends BaseFragment {

    @BindView(R.id.qr_code_button)
    Button makeQrCodeButton;

    @BindView(R.id.diagram_button)
    Button diagram;

    private Long sessionId;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getArguments() != null) {
            sessionId = getArguments().getLong("SESSION_ID");
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
}
