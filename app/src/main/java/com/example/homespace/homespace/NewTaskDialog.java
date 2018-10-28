package com.example.homespace.homespace;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NewTaskDialog extends DialogFragment implements View.OnClickListener {
    private static final String TAG = "NewTaskDialog";

    private EditText mTitle, mDescription;
    private TextView mCreate, mCancel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int style = DialogFragment.STYLE_NORMAL;
        int theme = android.R.style.Theme;
        setStyle(style, theme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_new_task, container, false);
        mTitle = view.findViewById(R.id.newTaskDialogTitleEditText);
        mDescription = view.findViewById(R.id.newTaskDialogDescriptionEditText);
        mCancel = view.findViewById(R.id.newTaskDialogCancelTextView);
        mCreate = view.findViewById(R.id.newTaskDialogCreateTextView);

        getDialog().setTitle("New Task");

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.newTaskDialogCreateTextView: {

                // insert new task

                String title = mTitle.getText().toString();
                String description = mDescription.getText().toString();

                if (!title.equals("")) {
                    getDialog().dismiss();
                } else {
                    Toast.makeText(getActivity(), "Enter a title", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.newTaskDialogCancelTextView: {
                getDialog().dismiss();
                break;
            }
        }
    }
}
