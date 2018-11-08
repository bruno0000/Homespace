package com.example.homespace.homespace;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.ArrayList;

public class MemberSelectionItemViewAdapter extends RecyclerView.Adapter
        <MemberSelectionItemViewAdapter.MemberSelectionItemViewHolder> {
    private ArrayList<User> mUserList;

    public class MemberSelectionItemViewHolder extends RecyclerView.ViewHolder {
        private CheckBox mCheckBoxUser;

        public MemberSelectionItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mCheckBoxUser = itemView.findViewById(R.id.checkBoxSelectMember);
        }
    }

    public MemberSelectionItemViewAdapter(ArrayList<User> userList) {
        mUserList = userList;
    }

    @NonNull
    @Override
    public MemberSelectionItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_select_member_item, viewGroup, false);
        return new MemberSelectionItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberSelectionItemViewHolder memberSelectionItemViewHolder, int i) {
        User currentUser = mUserList.get(i);
        memberSelectionItemViewHolder.mCheckBoxUser.setText(currentUser.getUsername());
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

}
