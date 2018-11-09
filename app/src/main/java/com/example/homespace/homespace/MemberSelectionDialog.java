package com.example.homespace.homespace;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MemberSelectionDialog extends DialogFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private DocumentSnapshot mLastQueriedDocument;

    private ArrayList<User> mUserList;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private String mHomespaceID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_member_selection, container, false);
        Button mCancel = v.findViewById(R.id.selectMemberDialogCancelButton);
        Button mConfirm = v.findViewById(R.id.selectMemberDialogConfirmButton);
        mCancel.setOnClickListener(this);
        mConfirm.setOnClickListener(this);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        mUserList = new ArrayList<>();

        mRecyclerView = v.findViewById(R.id.selectMemberRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new MemberSelectionItemViewAdapter(mUserList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout = v.findViewById(R.id.memberSelectionSwipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);


        getDialog().setTitle("Assign Members (Swipe down to refresh list)");

        getHomespaceID();
        getUsers();

        return v;
    }

    @Override
    public void onRefresh() {
        mUserList.clear();
        mLastQueriedDocument = null;
        getUsers();
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selectMemberDialogCancelButton: {
                getDialog().dismiss();
                break;
            }
            case R.id.selectMemberDialogConfirmButton: {
                getDialog().dismiss();
                break;
            }
        }
    }

    private void getHomespaceID() {
        String userUID = mAuth.getUid();

        DocumentReference docRef = db.collection("users").document(userUID);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    User user = task.getResult().toObject(User.class);
                    mHomespaceID = user.getHomespaceID();
                }
            }
        });
    }

    private void getUsers() {
        getHomespaceID();

        CollectionReference usersRef = db.collection("users");
        Query usersQuery;
        if (mLastQueriedDocument != null) {
            usersQuery = usersRef.whereEqualTo("homespaceID", mHomespaceID)
                    .startAfter(mLastQueriedDocument); // for no duplicates on refresh
        } else {
            usersQuery = usersRef.whereEqualTo("homespaceID", mHomespaceID);
        }

        usersQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        User userDoc = document.toObject(User.class);
                        mUserList.add(userDoc);
                    }
                    if (task.getResult().size() != 0) {
                        mLastQueriedDocument = task.getResult().getDocuments()
                                .get(task.getResult().size() - 1);
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "Query Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
