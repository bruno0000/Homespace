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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FinanceFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "FinanceFragment";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<Finance> mFinanceList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_finances, container, false);
        v.findViewById(R.id.newFinanceFloatingActionButton).setOnClickListener(this);

        mFinanceList = new ArrayList<>();

        mRecyclerView = v.findViewById(R.id.financeFragmentRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mAdapter = new FinanceItemRecyclerViewAdapter(mFinanceList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        getFinances();

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newFinanceFloatingActionButton: {
                // start new finance activity
                Intent intent = new Intent(getActivity(), NewFinanceActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    private void getFinances() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference financesReference = db.collection("finances");
        Query financeQuery = financesReference
                .whereEqualTo("userUID", FirebaseAuth.getInstance().getUid());

        financeQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Finance finance = document.toObject(Finance.class);
                        mFinanceList.add(finance);
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(getActivity(), "Query Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
