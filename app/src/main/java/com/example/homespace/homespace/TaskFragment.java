package com.example.homespace.homespace;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class TaskFragment extends Fragment {

    private static final String TAG = "TaskFragment";

    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    private Calendar cal;
    private int year, month, day, hour, minute;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tasks, container, false);

        Button btnDatePicker;
        Button btnTimePicker;
        Button btnDTConfirm;
        cal = Calendar.getInstance();

        btnDatePicker = (Button) v.findViewById(R.id.buttonDatePicker);
        btnTimePicker = (Button) v.findViewById(R.id.buttonTimePicker);
        btnDTConfirm = (Button) v.findViewById(R.id.buttonDTConfirm);

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        android.R.style.Theme_DeviceDefault_Light,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.setCancelable(false);
                dialog.show();
            }
        });

        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog dialog = new TimePickerDialog(getActivity(),
                        android.R.style.Theme_DeviceDefault_Light,
                        mTimeSetListener,
                        hour, minute,
                        DateFormat.is24HourFormat(getActivity()));
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.setCancelable(false);
                dialog.show();
            }
        });

        /*
        * Get the Calendar values for the database here
        * */
        btnDTConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                cal.set(year, month, day, hour, minute);
                Log.d("TaskFragment", "Cal.YEAR/MONTH/DAY_OF_MONTH/HOUR/MINUTE: " +
                        cal.get(cal.YEAR) + "/" + cal.get(cal.MONTH) + "/" + cal.get(cal.DAY_OF_MONTH) +
                        " " + cal.get(cal.HOUR) + ":" + cal.get(cal.MINUTE));
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int yy, int mm, int dd) {
                String date = yy + "/" + mm + "/" + dd;
                year = yy;
                month = mm;
                day = dd;
                Log.d("TaskFragment", "OnDateSet: year/month/day: " + date);
            }
        };

        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hh, int mm) {
                String time = hh + ":" + mm;
                hour = hh;
                minute = mm;
                Log.d("TaskFragment", "OnTimeSet: " + time);
            }
        };

        return v;
    }
}