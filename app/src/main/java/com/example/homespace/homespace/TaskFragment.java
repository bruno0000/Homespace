package com.example.homespace.homespace;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class TaskFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "TaskFragment";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tasks, container, false);
        v.findViewById(R.id.buttonTestCalendar).setOnClickListener(this);
        v.findViewById(R.id.newTaskFloatingActionButton).setOnClickListener(this);

        ArrayList<Task> taskList = new ArrayList<>();

        for (int i = 0; i < 10; i++){
            Task task = new Task();
            task.setTitle("Title" + i);
            task.setDescription("Description" + i);
            task.setImageResource(R.drawable.ic_notifications_black_24dp);

            taskList.add(task);

        }

        mRecyclerView = v.findViewById(R.id.taskFragmentRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mAdapter = new TaskItemRecyclerViewAdapter(taskList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        return v;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newTaskFloatingActionButton: {
                /*
                NewTaskDialog dialog = new NewTaskDialog();
                dialog.show(getActivity().getSupportFragmentManager(), "newTaskDialog . show");
*/
                Intent intent = new Intent(getActivity(), NewTaskActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            }
            case R.id.buttonTestCalendar: {
                Intent intent = new Intent(getActivity(), CalendarActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}