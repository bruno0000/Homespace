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
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FinanceItemRecyclerViewAdapter extends RecyclerView.Adapter<FinanceItemRecyclerViewAdapter.FinanceItemViewHolder> {
    private ArrayList<Finance> mFinanceList;

    public static class FinanceItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextViewTitle;
        public TextView mTextViewDescription;
        public TextView mAmountTextView;
        public TextView mTextViewDueDateTimeLabel;
        public TextView mTextViewDueDateTime;

        public FinanceItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageViewFinanceItemImage);
            mTextViewTitle = itemView.findViewById(R.id.textViewFinanceItemTitle);
            mTextViewDescription = itemView.findViewById(R.id.textViewFinanceItemDescription);
            mAmountTextView = itemView.findViewById(R.id.textViewFinanceItemAmount);
            mTextViewDueDateTime = itemView.findViewById(R.id.textViewDueDateTime);
            mTextViewDueDateTimeLabel = itemView.findViewById(R.id.textViewDueDateTimeLabel);

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
        Calendar cal = Calendar.getInstance();
        Date date;
        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd, 'at' hh:mm a", Locale.getDefault());
        List<Integer> dueDateList = currentFinance.getDueDate();
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

        viewHolder.mImageView.setImageResource(currentFinance.getImageResource());
        viewHolder.mTextViewTitle.setText(currentFinance.getTitle());
        viewHolder.mTextViewDescription.setText(currentFinance.getDescription());
        String amount = curFormat.format(currentFinance.getAmount());
        viewHolder.mAmountTextView.setText(amount);
        viewHolder.mTextViewDueDateTime.setText(dueDateDisplay);

    }

    @Override
    public int getItemCount() {
        return mFinanceList.size();
    }
}
