package com.example.admin.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.admin.R;
import com.example.admin.service.FireBaseDataManager;
import com.example.admin.service.OnCreateSessionListener;
import com.example.admin.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateSessionFragment extends BaseFragment implements OnCreateSessionListener {

    @BindView(R.id.session_name_edit_text)
    EditText sessionNameEditText;

    @BindView(R.id.private_check_box)
    CheckBox isPrivateCheckBox;

    @BindView(R.id.card_spinner)
    Spinner cardSpinner;

    @BindView(R.id.create_session_button)
    Button createSessionButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_create_session, container, false);
            ButterKnife.bind(this, rootView);
        }
        initViews();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initViews() {
        final ArrayList<String> cardsStrings = new ArrayList<>(Arrays.asList(Utils.cards));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                rootView.getContext(), android.R.layout.simple_spinner_item, cardsStrings);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        cardSpinner.setAdapter(adapter);

        createSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireBaseDataManager.getInstance().createSession(
                        sessionNameEditText.getText().toString().trim(),
                        isPrivateCheckBox.isChecked(),
                        cardSpinner.getSelectedItemPosition(),
                        rootView.getContext()
                );
            }


        });


    }


}
