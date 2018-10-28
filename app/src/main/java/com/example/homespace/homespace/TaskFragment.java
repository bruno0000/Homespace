package com.example.homespace.homespace;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class TaskFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "TaskFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tasks, container, false);

        Button buttonTestCalendar;
        buttonTestCalendar = (Button) v.findViewById(R.id.buttonTestCalendar);

        buttonTestCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CalendarActivity.class);
                startActivity(intent);
            }
        });
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.newTaskFloatingActionButton: {
                NewTaskDialog dialog = new NewTaskDialog();
                dialog.show(getActivity().getSupportFragmentManager(), getString(R.string.dialog_new_task));
                break;
            }
        }
    }
}