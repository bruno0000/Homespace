package com.example.homespace.homespace;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEditTextUsername, mEditTextPassword, mEditTextConfirmPW;
    private ProgressBar mProgressBarLogin;
    private TextView mTextViewShowLogin, mTextViewShowRegister;
    private CheckBox mStaySignedInCheckBox;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private final String FAKE_EMAIL_DOMAIN = "@homespace.users";
    private final String TAG = "LoginActivity";
    private final String KEY_USERNAME = "username";
    private final String KEY_USER_ID = "userID";
    private final String KEY_USER_UID = "userUID";
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
        mStaySignedInCheckBox = findViewById(R.id.staySignedInCheckBox);

        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        findViewById(R.id.loginButton).setOnClickListener(this);
        findViewById(R.id.registerButton).setOnClickListener(this);
        mTextViewShowLogin.setOnClickListener(this);
        mTextViewShowRegister.setOnClickListener(this);

        // check if user is currently logged in
        if (mStaySignedInCheckBox.isChecked()) {
            checkUserState();
        }
        mAuth.signOut();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginButton: {
                String username = mEditTextUsername.getText().toString().trim();
                String password = mEditTextPassword.getText().toString().trim();
                userLogin(username, password);
                break;
            }
            case R.id.registerButton: {
                createUser();
                break;
            }
            case R.id.returningUserTextView: {
                showLoginForm();
                break;
            }
            case R.id.signUpTextView: {
                showRegisterForm();
                break;
            }
        }

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
        String name = mEditTextUsername.getText().toString().trim();
        String userID = mAuth.getUid();

        DocumentReference userRef = db.collection("users").document(userID);
        User user = new User();

        user.setUserUID(userID);
        user.setUsername(name);

        userRef.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "User Saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void userLogin(String username, String password) {
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

        mProgressBarLogin.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            verifyHomespace();
                        } else {
                            mProgressBarLogin.setVisibility(View.INVISIBLE);
                            Toast.makeText(LoginActivity.this,
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // checks the db collection to see if the currently logged in user has a homespace with their UID
    private void verifyHomespace() {
        final String userID = FirebaseAuth.getInstance().getUid();
        CollectionReference reference = db.collection("homespaces");
        Query userListQuery = null;
        if (userID != null) {
            userListQuery = reference.whereArrayContains("userList", userID);
            userListQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Homespace homespace = document.toObject(Homespace.class);
                            if (homespace.getUserList().contains(userID)){
                                startMainActivity();
                                return;
                            }
                        }
                    }
                }
            });
        }
        startHomespaceActivity();
        mProgressBarLogin.setVisibility(View.INVISIBLE);
    }

    private void checkUserState() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // User is signed in
            mProgressBarLogin.setVisibility(View.VISIBLE);
            verifyHomespace();
        } else {
            // User is signed out
            mProgressBarLogin.setVisibility(View.INVISIBLE);
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }
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
