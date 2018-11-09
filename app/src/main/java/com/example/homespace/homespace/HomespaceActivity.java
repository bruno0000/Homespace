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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class HomespaceActivity extends AppCompatActivity implements View.OnClickListener {
    private final String LOG = "HomespaceActivity";

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
                break;
            }
        }
    }

    private void saveHomespace() {
        String name = mEditTextName.getText().toString();
        String creatorUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference homespaceRef = db.collection("homespaces").document();

        String homespaceID = homespaceRef.getId();

        Homespace homespace = new Homespace();

        homespace.setHomespaceCreatorUID(creatorUID);
        homespace.setHomespaceName(name);
        homespace.setHomespaceID(homespaceID);
        List<String> userList = new ArrayList<>();
        userList.add(creatorUID);
        homespace.setUserList(userList);

        // update current user's document to have this homespace's ID for data modeling
        db.collection("users").document(creatorUID)
                .update("homespaceID", homespaceID)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(HomespaceActivity.this, "user homespaceID updated", Toast.LENGTH_SHORT).show();
                        } else {

                        }
                    }
                });

        // add new homespace to DB
        homespaceRef.set(homespace)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(HomespaceActivity.this, "Homespace Saved", Toast.LENGTH_SHORT).show();
                            startMainActivity();
                        } else {

                        }
                    }
                });
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }


}
