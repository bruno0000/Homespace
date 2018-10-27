package com.example.homespace.homespace;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword, editTextConfirmPW;
    private ProgressBar progressBarLogin;
    private TextView textViewShowLogin, textViewShowRegister;
    private FirebaseAuth mAuth;

    private final String FAKE_EMAIL_DOMAIN = "@superfakeemaildomain.netcom";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBarLogin = findViewById(R.id.loginProgressBar);
        progressBarLogin.setVisibility(View.INVISIBLE);
        editTextUsername = findViewById(R.id.usernameEditText);
        editTextPassword = findViewById(R.id.passwordEditText);
        editTextConfirmPW = findViewById(R.id.confirmPasswordEditText);
        textViewShowLogin = findViewById(R.id.returningUserTextView);
        textViewShowRegister = findViewById(R.id.signUpTextView);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                userLogin(username, password);
            }
        });


        findViewById(R.id.registerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });

        textViewShowLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginForm();
            }
        });

        textViewShowRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterForm();
            }
        });
    }

    private void createUser() {
        final String username = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String passwordConfirm = editTextConfirmPW.getText().toString().trim();

        if (username.isEmpty()) {
            editTextUsername.setError("Username Required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextUsername.setError("Password Required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextUsername.setError("Password should be at least 6 characters long");
            editTextPassword.requestFocus();
            return;
        }

        if (!password.equals(passwordConfirm)){
            editTextConfirmPW.setError("Passwords do not match");
            editTextConfirmPW.requestFocus();
            return;
        }

        progressBarLogin.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            startHomespaceActivity();
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                userLogin(username, password);
                            } else {
                                progressBarLogin.setVisibility(View.INVISIBLE);
                                Toast.makeText(LoginActivity.this,
                                        task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void userLogin(String username, String password) {
        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            startHomespaceActivity();
                        } else {
                            progressBarLogin.setVisibility(View.INVISIBLE);
                            Toast.makeText(LoginActivity.this,
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void startHomespaceActivity() {
        Intent intent = new Intent(this, HomespaceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }


    private void showLoginForm() {
        editTextConfirmPW.setVisibility(View.INVISIBLE);
        textViewShowLogin.setVisibility(View.INVISIBLE);
        findViewById(R.id.registerButton).setVisibility(View.INVISIBLE);
        findViewById(R.id.loginButton).setVisibility(View.VISIBLE);
        textViewShowRegister.setVisibility(View.VISIBLE);

    }

    private void showRegisterForm() {
        editTextConfirmPW.setVisibility(View.VISIBLE);
        textViewShowLogin.setVisibility(View.VISIBLE);
        findViewById(R.id.registerButton).setVisibility(View.VISIBLE);
        findViewById(R.id.loginButton).setVisibility(View.INVISIBLE);
        textViewShowRegister.setVisibility(View.INVISIBLE);
    }
}
