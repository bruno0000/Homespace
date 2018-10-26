package com.example.homespace.homespace;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CalendarView;

public class CalendarActivity extends AppCompatActivity {

    private static final String TAG = "CalenderActivity";

    private CalendarView mCalenderView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calender_layout);
        mCalenderView = (CalendarView) findViewById(R.id.calendarView);

        mCalenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {
                String date = i + "/" + i1 + "/" + i2;
                Log.d(TAG, "onSelectedDayChange: date: " + date);
            }
        });
    }
}
