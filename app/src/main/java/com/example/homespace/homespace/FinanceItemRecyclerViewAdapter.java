package com.example.homespace.homespace;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;

public class FinanceItemRecyclerViewAdapter extends RecyclerView.Adapter<FinanceItemRecyclerViewAdapter.FinanceItemViewHolder> {
    private ArrayList<Finance> mFinanceList;

    public static class FinanceItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextViewTitle;
        public TextView mTextViewDescription;
        public TextView mAmountTextView;

        public FinanceItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageViewFinanceItemImage);
            mTextViewTitle = itemView.findViewById(R.id.textViewFinanceItemTitle);
            mTextViewDescription = itemView.findViewById(R.id.textViewFinanceItemDescription);
            mAmountTextView = itemView.findViewById(R.id.textViewFinanceItemAmount);

        }
    }

    public FinanceItemRecyclerViewAdapter(ArrayList<Finance> financeList) {
        mFinanceList = financeList;
    }

    @NonNull
    @Override
    public FinanceItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_finance_item, viewGroup, false);
        return new FinanceItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FinanceItemViewHolder viewHolder, int i) {
        Finance currentFinance = mFinanceList.get(i);
        NumberFormat curFormat = NumberFormat.getCurrencyInstance();

        viewHolder.mImageView.setImageResource(currentFinance.getImageResource());
        viewHolder.mTextViewTitle.setText(currentFinance.getTitle());
        viewHolder.mTextViewDescription.setText(currentFinance.getDescription());
        String amount = curFormat.format(currentFinance.getAmount());
        viewHolder.mAmountTextView.setText(amount);

    }

    @Override
    public int getItemCount() {
        return mFinanceList.size();
    }
}
