package com.example.homespace.homespace;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class HomespaceActivity extends AppCompatActivity {
    private final String LOG_TAG = "HomespaceActivity";

    private static final String KEY_HOMESPACE_NAME = "homespaceName";
    private static final String KEY_HOMESPACE_CREATOR = "homespaceCreator";

    private EditText mEditTextName;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homespace);

        mEditTextName = findViewById(R.id.nameHomespaceEditText);
    }

    public void saveHomespace(View view) {
        String name = mEditTextName.getText().toString();
        String creator = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Map<String, Object> homespace = new HashMap<>();
        homespace.put(KEY_HOMESPACE_NAME, name);
        homespace.put(KEY_HOMESPACE_CREATOR, creator);

        db.collection("homespaces").document().set(homespace)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(HomespaceActivity.this, "Homespace Saved", Toast.LENGTH_SHORT).show();
                        startMainActivity();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HomespaceActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        //TODO: Remove before hand in
                        Log.d(LOG_TAG, e.toString());
                    }
                });
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
}
