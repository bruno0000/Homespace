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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;

public class TaskFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "TaskFragment";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Calendar cal;

    private ArrayList<Task> mTaskList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tasks, container, false);
        v.findViewById(R.id.newTaskFloatingActionButton).setOnClickListener(this);

        mTaskList = new ArrayList<>();

        mRecyclerView = v.findViewById(R.id.taskFragmentRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mAdapter = new TaskItemRecyclerViewAdapter(mTaskList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        getTasks();

        return v;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newTaskFloatingActionButton: {
                Intent intent = new Intent(getActivity(), NewTaskActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            }
        }
    }

    private void getTasks(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference tasksCollectionReference = db.collection("tasks");
        Query tasksQuery = tasksCollectionReference
                .whereEqualTo("userUID", FirebaseAuth.getInstance().getUid());

        tasksQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document: task.getResult()){
                        Task taskDocument = document.toObject(Task.class);
                        mTaskList.add(taskDocument);
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(getActivity(), "Query Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}