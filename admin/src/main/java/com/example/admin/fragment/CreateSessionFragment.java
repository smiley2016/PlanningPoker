package com.example.admin.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.admin.R;
import com.example.admin.util.FragmentNavigation;
import com.example.admin.util.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

public class CreateSessionFragment extends BaseFragment {

    @BindView(R.id.session_name_edit_text)
    EditText sessionNameEditText;

    @BindView(R.id.private_check_box)
    CheckBox isPrivateCheckBox;

    @BindView(R.id.card_spinner)
    Spinner cardSpinner;

    @BindView(R.id.create_session_button)
    Button createSessionButton;

    private static final String TAG = CreateSessionFragment.class.getName();

    private String sessionName;
    private boolean isPrivate;
    private int indexOfCard;
    private Long maxSessionIdInDb;

    public static String SESSION_NAME = "SESSION_NAME";
    public static String IS_PRIVATE = "IS_PRIVATE";
    public static String INDEX_OF_CARD = "INDEX_OF_CARD";
    public static String MAX_SESSION_ID_IN_DB = "MAX_SESSION_ID_IN_DB";

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
        ArrayList<String> cardsStrings = new ArrayList<>(Arrays.asList(Utils.cards));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                rootView.getContext(), android.R.layout.simple_spinner_item, cardsStrings);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        cardSpinner.setAdapter(adapter);

        createSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog dialog = new SpotsDialog(rootView.getContext(), R.style.Custom);
                dialog.show();
                sessionName = sessionNameEditText.getText().toString().trim();
                isPrivate = isPrivateCheckBox.isChecked();
                indexOfCard = cardSpinner.getSelectedItemPosition();

                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Session")
                        .orderBy("SessionId", Query.Direction.DESCENDING)
                        .limit(1)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful() && task.getResult() != null) {
                                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                        maxSessionIdInDb = (Long) snapshot.getData().get("SessionId");
                                    }

                                    if (maxSessionIdInDb == null) {
                                        maxSessionIdInDb = Long.parseLong("0");
                                    }
                                    Map<String, Object> session = new HashMap<>();
                                    session.put("SessionId", maxSessionIdInDb + 1);
                                    session.put("SessionName", sessionName);
                                    session.put("IsPrivate", isPrivate);
                                    session.put("IndexOfCard", indexOfCard);
                                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                        session.put("UID", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    }

                                    db.collection("Session")
                                            .add(session)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                                    Toast.makeText(rootView.getContext(),
                                                            "DocumentSnapshot added with ID: "
                                                                    + documentReference.getId(),
                                                            Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();
                                                    Bundle bundle = new Bundle();
                                                    bundle.putLong("SESSION_ID", maxSessionIdInDb + 1);
                                                    FragmentNavigation.getInstance(rootView.getContext())
                                                            .showCreateQuestionFragment(bundle);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.e(TAG, e.toString());
                                                    dialog.dismiss();
                                                    Toast.makeText(rootView.getContext(),
                                                            "Query failure: "
                                                                    + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }

                            }
                        });
            }


        });


    }


}
