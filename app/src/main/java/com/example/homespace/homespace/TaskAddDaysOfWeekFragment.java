package com.example.homespace.homespace;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TaskAddDaysOfWeekFragment extends Fragment {

    private static final String TAG = "TaskFragment";

    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    private Calendar cal;
    private int hour, minute;
    private boolean days[] = { false, false, false, false, false, false, false };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tasks_daysofweek, container, false);

        Button btnTimePicker;
        Button btnDTConfirm;
        final CheckBox chkboxMonday;
        final CheckBox chkboxTuesday;
        final CheckBox chkboxWednesday;
        final CheckBox chkboxThursday;
        final CheckBox chkboxFriday;
        final CheckBox chkboxSaturday;
        final CheckBox chkboxSunday;
        cal = Calendar.getInstance();

        chkboxMonday = v.findViewById(R.id.checkBoxMonday);
        chkboxTuesday = v.findViewById(R.id.checkBoxTuesday);
        chkboxWednesday = v.findViewById(R.id.checkBoxWednesday);
        chkboxThursday = v.findViewById(R.id.checkBoxThursday);
        chkboxFriday = v.findViewById(R.id.checkBoxFriday);
        chkboxSaturday = v.findViewById(R.id.checkBoxSaturday);
        chkboxSunday = v.findViewById(R.id.checkBoxSunday);
        btnTimePicker = v.findViewById(R.id.buttonTimeDaysPicker);
        btnDTConfirm = v.findViewById(R.id.buttonDTDaysConfirm);

        chkboxMonday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                days[0] = chkboxMonday.isChecked();
                Log.d("TaskFragment", "day 0: " + days[0]);
            }
        });
        chkboxTuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                days[1] = chkboxTuesday.isChecked();
                Log.d("TaskFragment", "day 1: " + days[1]);
            }
        });
        chkboxWednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                days[2] = chkboxWednesday.isChecked();
                Log.d("TaskFragment", "day 2: " + days[2]);
            }
        });
        chkboxThursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                days[3] = chkboxThursday.isChecked();
                Log.d("TaskFragment", "day 3: " + days[3]);
            }
        });
        chkboxFriday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                days[4] = chkboxFriday.isChecked();
                Log.d("TaskFragment", "day 4: " + days[4]);
            }
        });
        chkboxSaturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                days[5] = chkboxSaturday.isChecked();
                Log.d("TaskFragment", "day 5: " + days[5]);
            }
        });
        chkboxSunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                days[6] = chkboxSunday.isChecked();
                Log.d("TaskFragment", "day 6: " + days[6]);
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
            }
        });

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