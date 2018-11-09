package com.example.homespace.homespace;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewFinanceActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "NewFinanceActivity";
    private EditText mTitle, mDescription, mAmount;
    private TextView mCreate, mCancel, mEditDate, mEditTime;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    private Calendar cal;

    private int year, month, day, hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_finance);

        mTitle = findViewById(R.id.newFinanceTitleEditText);
        mDescription = findViewById(R.id.newFinanceDescriptionEditText);
        mAmount = findViewById(R.id.newFinanceAmountEditText);
        mCancel = findViewById(R.id.newFinanceCancelTextView);
        mCreate = findViewById(R.id.newFinanceCreateTextView);
        mEditDate = findViewById(R.id.newFinanceDueDateEditText);
        mEditTime = findViewById(R.id.newFinanceDueTimeEditText);

        mCancel.setOnClickListener(this);
        mCreate.setOnClickListener(this);
        mEditDate.setOnClickListener(this);
        mEditTime.setOnClickListener(this);
        findViewById(R.id.newFinanceMembersButton).setOnClickListener(this);

        cal = Calendar.getInstance();



        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int yy, int mm, int dd) {
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");
                cal.set(Calendar.YEAR, yy);
                cal.set(Calendar.MONTH, mm);
                cal.set(Calendar.DAY_OF_MONTH, dd);
                Date date = cal.getTime();
                mEditDate.setText(dateFormatter.format(date));
            }
        };
        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hh, int mm) {
                SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
                cal.set(Calendar.HOUR_OF_DAY, hh);
                cal.set(Calendar.MINUTE, mm);
                Date time = cal.getTime();
                mEditTime.setText(timeFormatter.format(time));
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.newFinanceDueDateEditText: {
                selectDate();
                break;
            }
            case R.id.newFinanceDueTimeEditText: {
                selectTime();
                break;
            }
            case R.id.newFinanceCreateTextView: {
                saveFinance();
                this.finish();
                break;
            }
            case R.id.newFinanceCancelTextView: {
                this.finish();
                break;
            }
            case R.id.newFinanceMembersButton: {
                DialogFragment memberSelectionDialog = new MemberSelectionDialog();
                memberSelectionDialog.show(getSupportFragmentManager(),"Select Members");
                break;
            }
        }
    }

    private void selectDate() {
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this,
                android.R.style.Theme_DeviceDefault_Light,
                mDateSetListener,
                year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setCancelable(false);
        dialog.show();
    }

    private void selectTime(){
        TimePickerDialog dialog = new TimePickerDialog(this,
                android.R.style.Theme_DeviceDefault_Light,
                mTimeSetListener,
                hour, minute,
                DateFormat.is24HourFormat(this));
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setCancelable(false);
        dialog.show();
    }

    private void saveFinance(){
        String title = mTitle.getText().toString();
        String description = mDescription.getText().toString();
        List<Integer> date = new ArrayList(5);
        double amount;

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
        if(!mEditDate.getText().toString().equals("") && !mEditTime.getText().toString().equals("")) {
            date.add(year);
            date.add(month);
            date.add(day);
            date.add(hour);
            date.add(minute);
            finance.setDueDate(date);
        } else {
            mEditDate.setError("Enter a completion date and time");
            Toast.makeText(this, "Enter a completion date and time", Toast.LENGTH_SHORT).show();
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
    }
}
