package com.example.homespace.homespace;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewTaskActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "NewTaskActivity";
    private EditText mTitle, mDescription;
    private TextView mCreate, mCancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        mTitle = findViewById(R.id.newTaskDialogTitleEditText);
        mDescription = findViewById(R.id.newTaskDialogDescriptionEditText);
        mCancel = findViewById(R.id.newTaskDialogCancelTextView);
        mCreate = findViewById(R.id.newTaskDialogCreateTextView);
        mCancel.setOnClickListener(this);
        mCreate.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newTaskDialogCreateTextView: {

                // insert new task

                String title = mTitle.getText().toString();
                String description = mDescription.getText().toString();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference newTaskRef = db.collection("tasks").document();
                Task task = new Task();

                task.setTitle(title);
                task.setDescription(description);
                task.setTaskID(newTaskRef.getId());
                task.setUserID(FirebaseAuth.getInstance().getUid());

                newTaskRef.set(task).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(NewTaskActivity.this, "New Task Saved", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(NewTaskActivity.this, "New Task Unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                if (!title.equals("")) {
                } else {
                    Toast.makeText(this, "Enter a title", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.newTaskDialogCancelTextView: {
                this.finish();
                break;
            }
            case R.id.buttonnewtaskcanceltest: {
                finish();
                break;
            }
        }
    }


}
