package com.example.homespace.homespace;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TaskItemRecyclerViewAdapter extends RecyclerView.Adapter<TaskItemRecyclerViewAdapter.TaskItemViewHolder> {
    private ArrayList<Task> mTaskList;

    public static class TaskItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextViewTitle;
        public TextView mTextViewDesctiption;

        public TaskItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageViewTaskItemImage);
            mTextViewTitle = itemView.findViewById(R.id.textViewTaskItemTitle);
            mTextViewDesctiption = itemView.findViewById(R.id.textViewTaskItemDescription);
        }
    }

    public TaskItemRecyclerViewAdapter(ArrayList<Task> taskArrayList) {
        mTaskList = taskArrayList;
    }

    @NonNull
    @Override
    public TaskItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_task_item, viewGroup, false);
        return new TaskItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskItemViewHolder taskItemViewHolder, int i) {
        Task currentTask = mTaskList.get(i);

        taskItemViewHolder.mImageView.setImageResource(currentTask.getImageResource());
        taskItemViewHolder.mTextViewTitle.setText(currentTask.getTitle());
        taskItemViewHolder.mTextViewDesctiption.setText(currentTask.getDescription());

    }

    @Override
    public int getItemCount() {
        return mTaskList.size();
    }


}
