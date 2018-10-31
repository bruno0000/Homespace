package com.example.homespace.homespace;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class FinanceItemRecyclerViewAdapter extends RecyclerView.Adapter {
    private ArrayList<Finance> mFinanceList;

    public static class FinanceItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextViewTitle;
        public TextView mTextViewDescription;

        public FinanceItemViewHolder(@NonNull View itemView) {
            super(itemView);
            // mImageView = itemView.findViewByID();
        }
    }

    public FinanceItemRecyclerViewAdapter(ArrayList<Finance> financeList) {
        mFinanceList = financeList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    //    View v = LayoutInflater.from(viewGroup.getContext())
    //            .inflate(R.layout., viewGroup, false);

    //    return new FinanceItemViewHolder(v);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Finance currentFinance = mFinanceList.get(i);

    }

    @Override
    public int getItemCount() {
        return mFinanceList.size();
    }
}
