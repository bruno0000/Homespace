package com.example.homespace.homespace;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TaskFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tasks, null);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.newTaskFloatingActionButton: {

                NewTaskDialog dialog = new NewTaskDialog();
                dialog.show(getFragmentManager().beginTransaction(), getString(R.string.dialog_new_task));
            }
        }
    }
}
