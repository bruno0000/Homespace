package com.example.homespace.homespace;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class HomespaceActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "HomespaceActivity";

    private EditText mEditTextName;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homespace);
        mEditTextName = findViewById(R.id.nameHomespaceEditText);
        findViewById(R.id.nameHomespaceButton).setOnClickListener(this);
        findViewById(R.id.joinHomespaceTextView).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        setupFirebaseListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nameHomespaceButton: {
                saveHomespace();
                break;
            }
            case R.id.joinHomespaceTextView: {
                TextView textView = findViewById(R.id.joinHomespaceTextView);
                textView.setText(getString(R.string.sorry));
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sign_out, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.signOutMenuButton: {
                FirebaseAuth.getInstance().signOut();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void saveHomespace() {
        String name = mEditTextName.getText().toString().trim();
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

        // add new homespace to DB
        homespaceRef.set(homespace)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(HomespaceActivity.this, "Homespace Saved", Toast.LENGTH_SHORT).show();
                            startMainActivity();
                        } else {
                            Toast.makeText(HomespaceActivity.this, "Failed to save Homespace", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // update current user's document to have this homespace's ID
        db.collection("users").document(creatorUID)
                .update("homespaceID", homespaceID)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(HomespaceActivity.this, "User Homespace Updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(HomespaceActivity.this, "Failed to update user Homespace", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }


    private void setupFirebaseListener() {
        Log.d(TAG, "setupFirebaseListener: setting up the auth state listener");
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged: signed_in " + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                    Toast.makeText(HomespaceActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomespaceActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        };
    }
}
