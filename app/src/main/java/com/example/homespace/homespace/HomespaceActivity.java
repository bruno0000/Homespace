package com.example.homespace.homespace;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomespaceActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String KEY_HOMESPACE_ID = "homespaceID";
    private final String LOG = "HomespaceActivity";

    private static final String KEY_HOMESPACE_NAME = "homespaceName";
    private static final String KEY_HOMESPACE_CREATOR = "homespaceCreator";

    private EditText mEditTextName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homespace);
        mEditTextName = findViewById(R.id.nameHomespaceEditText);
        findViewById(R.id.nameHomespaceButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nameHomespaceButton: {
                saveHomespace();
            }
        }
    }

    private void saveHomespace() {
        String name = mEditTextName.getText().toString();
        String creator = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference homespaceRef = db.collection("homespaces").document();

        Homespace homespace = new Homespace();

        homespace.setHomespaceCreatorUID(creator);
        homespace.setHomespaceName(name);
        homespace.setHomespaceID(homespaceRef.getId());

        // TODO: update current user's document to have this homespace's ID for data modeling

        homespaceRef.set(homespace)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(HomespaceActivity.this,
                                "Homespace Saved", Toast.LENGTH_SHORT).show();
                        startMainActivity();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HomespaceActivity.this,
                                "Error", Toast.LENGTH_SHORT).show();
                        //TODO: Remove before hand in
                        Log.d(LOG, e.toString());
                    }
                });
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }


}
