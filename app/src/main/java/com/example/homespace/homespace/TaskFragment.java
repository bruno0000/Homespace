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

public class TaskFragment extends Fragment {

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


        v.findViewById(R.id.newTaskFloatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                NewTaskDialog dialog = new NewTaskDialog();
                dialog.show(getActivity().getSupportFragmentManager(), "newTaskDialog . show");
*/

                Intent intent = new Intent(getActivity(), NewTaskActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        return v;
    }

}