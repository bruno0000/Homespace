package com.example.homespace.homespace;

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

        mTitle = findViewById(R.id.newTaskTitleEditText);
        mDescription = findViewById(R.id.newTaskDescriptionEditText);
        mCancel = findViewById(R.id.newTaskCancelTextView);
        mCreate = findViewById(R.id.newTaskCreateTextView);
        mCancel.setOnClickListener(this);
        mCreate.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newTaskCreateTextView: {
                // insert new task
                String title = mTitle.getText().toString();
                String description = mDescription.getText().toString();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference newTaskRef = db.collection("tasks").document();
                Task task = new Task();

                if (!title.isEmpty()) {
                    task.setTitle(title);
                } else {
                    mTitle.setError("Enter a title");
                    mTitle.requestFocus();
                    Toast.makeText(this, "Enter a title", Toast.LENGTH_SHORT).show();
                    return;
                }
                task.setDescription(description);
                task.setTaskID(newTaskRef.getId());
                task.setUserUID(FirebaseAuth.getInstance().getUid());

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
                this.finish();
                break;
            }
            case R.id.newTaskCancelTextView: {
                this.finish();
                break;
            }
        }
    }


}
