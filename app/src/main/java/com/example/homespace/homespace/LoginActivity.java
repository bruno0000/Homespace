package com.example.homespace.homespace;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText mEditTextUsername, mEditTextPassword, mEditTextConfirmPW;
    private ProgressBar mProgressBarLogin;
    private TextView mTextViewShowLogin, mTextViewShowRegister;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private final String FAKE_EMAIL_DOMAIN = "@superfakeemaildomain.netcom";
    private final String LOG_TAG = "LoginActivityLOGTAG";
    private final String KEY_USERNAME = "username";
    private final String KEY_USER_ID = "userID";
    private final String KEY_HOMESPACE_ID = "homespaceID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mProgressBarLogin = findViewById(R.id.loginProgressBar);
        mProgressBarLogin.setVisibility(View.INVISIBLE);
        mEditTextUsername = findViewById(R.id.usernameEditText);
        mEditTextPassword = findViewById(R.id.passwordEditText);
        mEditTextConfirmPW = findViewById(R.id.confirmPasswordEditText);
        mTextViewShowLogin = findViewById(R.id.returningUserTextView);
        mTextViewShowRegister = findViewById(R.id.signUpTextView);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mEditTextUsername.getText().toString().trim();
                String password = mEditTextPassword.getText().toString().trim();
                userLogin(username, password);
            }
        });


        findViewById(R.id.registerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });

        mTextViewShowLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginForm();
            }
        });

        mTextViewShowRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterForm();
            }
        });
    }

    private void createUser() {
        // Note fake domain concatenation
        final String username = mEditTextUsername.getText().toString().trim();// + FAKE_EMAIL_DOMAIN;
        final String password = mEditTextPassword.getText().toString().trim();
        final String passwordConfirm = mEditTextConfirmPW.getText().toString().trim();

        if (username.isEmpty()) {
            mEditTextUsername.setError("Username Required");
            mEditTextUsername.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            mEditTextPassword.setError("Password Required");
            mEditTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            mEditTextPassword.setError("Password should be at least 6 characters long");
            mEditTextPassword.requestFocus();
            return;
        }

        if (!password.equals(passwordConfirm)) {
            mEditTextConfirmPW.setError("Passwords do not match");
            mEditTextConfirmPW.requestFocus();
            return;
        }

        mProgressBarLogin.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            saveNewUser();
                            startHomespaceActivity();
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                userLogin(username, password);
                            } else {
                                mProgressBarLogin.setVisibility(View.INVISIBLE);
                                Toast.makeText(LoginActivity.this,
                                        task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    public void saveNewUser() {
        String name = mEditTextUsername.getText().toString();
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Map<String, Object> user = new HashMap<>();
        user.put(KEY_USERNAME, name);
        user.put(KEY_USER_ID, userID);
        user.put(KEY_HOMESPACE_ID, "");

        db.collection("users").document().set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(LoginActivity.this, "User Saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();

                        //TODO: Remove before hand in
                        Log.d(LOG_TAG, e.toString());
                    }
                });
    }

    // TODO: Verify current user so they skip the creating homespace activity
    private void userLogin(String username, String password) {
        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            startHomespaceActivity();

                        } else {
                            mProgressBarLogin.setVisibility(View.INVISIBLE);
                            Toast.makeText(LoginActivity.this,
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    // Sends user to activity for creating or joining a homespace
    private void startHomespaceActivity() {
        Intent intent = new Intent(this, HomespaceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }


    // Replace the registration form with login form
    private void showLoginForm() {
        mEditTextConfirmPW.setVisibility(View.INVISIBLE);
        mTextViewShowLogin.setVisibility(View.INVISIBLE);
        findViewById(R.id.registerButton).setVisibility(View.INVISIBLE);
        findViewById(R.id.loginButton).setVisibility(View.VISIBLE);
        mTextViewShowRegister.setVisibility(View.VISIBLE);

    }

    // Replace login form with registration form
    private void showRegisterForm() {
        mEditTextConfirmPW.setVisibility(View.VISIBLE);
        mTextViewShowLogin.setVisibility(View.VISIBLE);
        findViewById(R.id.registerButton).setVisibility(View.VISIBLE);
        findViewById(R.id.loginButton).setVisibility(View.INVISIBLE);
        mTextViewShowRegister.setVisibility(View.INVISIBLE);
    }
}
