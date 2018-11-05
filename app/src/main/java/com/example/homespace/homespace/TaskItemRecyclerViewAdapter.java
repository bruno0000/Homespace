package com.example.homespace.homespace;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskItemRecyclerViewAdapter extends RecyclerView.Adapter<TaskItemRecyclerViewAdapter.TaskItemViewHolder> {
    private ArrayList<Task> mTaskList;

    public static class TaskItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextViewTitle;
        public TextView mTextViewDescription;
        public TextView mTextViewDueDateTimeLabel;
        public TextView mTextViewDueDateTime;

        public TaskItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageViewTaskItemImage);
            mTextViewTitle = itemView.findViewById(R.id.textViewTaskItemTitle);
            mTextViewDescription = itemView.findViewById(R.id.textViewTaskItemDescription);
            mTextViewDueDateTime = itemView.findViewById(R.id.textViewDueDateTime);
            mTextViewDueDateTimeLabel = itemView.findViewById(R.id.textViewDueDateTimeLabel);
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
        Calendar cal = Calendar.getInstance();
        Date date;
        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd, 'at' hh:mm", Locale.getDefault());
        List<Integer> dueDateList = currentTask.getDueDate();
        StringBuffer dueDateDisplay = new StringBuffer();
        int dateTimeIndex = 0;
        for(Integer item : dueDateList) {
            switch(dateTimeIndex) {
                case(0) : {
                    cal.set(Calendar.YEAR, item);
                    break;
                }
                case(1) : {
                    cal.set(Calendar.MONTH, item);
                    break;
                }
                case(2) : {
                    cal.set(Calendar.DAY_OF_MONTH, item);
                    break;
                }
                case(3) : {
                    cal.set(Calendar.HOUR, item);
                    break;
                }
                case(4) : {
                    cal.set(Calendar.MINUTE, item);
                    break;
                }
            }
            dateTimeIndex++;
        }
        date = cal.getTime();
        formatter.format(date, dueDateDisplay, new FieldPosition(DateFormat.DATE_FIELD));

        taskItemViewHolder.mImageView.setImageResource(currentTask.getImageResource());
        taskItemViewHolder.mTextViewTitle.setText(currentTask.getTitle());

        
        taskItemViewHolder.mTextViewDescription.setText(currentTask.getDescription());
        taskItemViewHolder.mTextViewDueDateTime.setText(dueDateDisplay);

    }

    @Override
    public int getItemCount() {
        return mTaskList.size();
    }


}
