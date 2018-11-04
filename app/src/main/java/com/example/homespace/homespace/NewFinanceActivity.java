package com.example.homespace.homespace;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewFinanceActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "NewFinanceActivity";
    private EditText mTitle, mDescription, mAmount;
    private TextView mCreate, mCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_finance);

        mTitle = findViewById(R.id.newFinanceTitleEditText);
        mDescription = findViewById(R.id.newFinanceDescriptionEditText);
        mAmount = findViewById(R.id.newFinanceAmountEditText);
        mCancel = findViewById(R.id.newFinanceCancelTextView);
        mCreate = findViewById(R.id.newFinanceCreateTextView);
        mCancel.setOnClickListener(this);
        mCreate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.newFinanceCreateTextView: {
                String title = mTitle.getText().toString();
                String description = mDescription.getText().toString();
                double amount = 0;
                if (!mAmount.getText().toString().isEmpty()){
                    amount = Double.parseDouble(mAmount.getText().toString());
                } else {
                    mAmount.setError("Enter an amount");
                    mAmount.requestFocus();
                    Toast.makeText(this, "Enter an amount", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference newFinanceRef = db.collection("finances").document();
                Finance finance = new Finance();

                if (!title.isEmpty()) {
                    finance.setTitle(title);
                } else {
                    mTitle.setError("Enter a title");
                    mTitle.requestFocus();
                    Toast.makeText(this, "Enter a title", Toast.LENGTH_SHORT).show();
                    return;
                }

                finance.setAmount(amount);
                finance.setDescription(description);
                finance.setFinanceID(newFinanceRef.getId());
                finance.setUserUID(FirebaseAuth.getInstance().getUid());

                newFinanceRef.set(finance).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(NewFinanceActivity.this, "New Finance Saved", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(NewFinanceActivity.this, "New Finance Unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                this.finish();
                break;
            }
            case R.id.newFinanceCancelTextView: {
                this.finish();
                break;
            }
        }
    }
}
