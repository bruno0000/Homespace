package com.example.homespace.homespace;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class UserItemRecyclerViewAdapter extends RecyclerView.Adapter<UserItemRecyclerViewAdapter.UserItemViewHolder> {
    private ArrayList<User> mUserList;

    public static class UserItemViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextViewUsername;
        public TextView mTextViewUID;


        public UserItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewUsername = itemView.findViewById(R.id.userItemUsernameTextView);
            mTextViewUID = itemView.findViewById(R.id.userItemUIDTextView);
        }
    }

    public UserItemRecyclerViewAdapter(ArrayList<User> userList) {
        mUserList = userList;
    }

    @NonNull
    @Override
    public UserItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_user_item, viewGroup, false);
        return new UserItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserItemViewHolder userItemViewHolder, int i) {
        User currentUser = mUserList.get(i);

        userItemViewHolder.mTextViewUsername.setText(currentUser.getUsername());
        userItemViewHolder.mTextViewUID.setText(currentUser.getUserUID());
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

}
