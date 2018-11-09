package com.example.homespace.homespace;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "HomeFragment";

    private Button mSignOut;
    private EditText mAddMemberEditText;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private DocumentSnapshot mLastQueriedDocument;

    private ArrayList<User> mUserList;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    // list will load users with null homespaceID on start, getHomespaceID() not working unless
    // user uses pull-down refresh then it loads correctly
    private String mHomespaceID = "x";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        mSignOut = v.findViewById(R.id.signOutButton);
        mSignOut.setOnClickListener(this);

        v.findViewById(R.id.addMemberButton).setOnClickListener(this);
        mAddMemberEditText = v.findViewById(R.id.addMemberEditText);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        mUserList = new ArrayList<>();

        mRecyclerView = v.findViewById(R.id.homespaceMemberListRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mAdapter = new UserItemRecyclerViewAdapter(mUserList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout = v.findViewById(R.id.homeFragmentUserListSwipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

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
            case R.id.signOutButton: {
                FirebaseAuth.getInstance().signOut();
                break;
            }
            case R.id.addMemberButton: {
                addMember();
                break;
            }
        }
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

    private void addMember() {
        final String username = mAddMemberEditText.getText().toString().trim();
        CollectionReference usersRef = db.collection("users");
        Query userQuery = usersRef.whereEqualTo("username", username);
        userQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        User user = document.toObject(User.class);
                        updateUserHomespaceID(user, mHomespaceID);
                        updateHomespaceUserList(user, mHomespaceID);
                    }
                }
            }
        });
    }

    private void updateUserHomespaceID(final User user, String homespaceID) {
        // update current user's document to have this homespace's ID for data modeling
        db.collection("users").document(user.getUserUID())
                .update("homespaceID", homespaceID)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Added " + user.getUsername(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Could not find user", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // updates the array of userID strings within the homespace document
    private void updateHomespaceUserList(final User user, String homespaceID) {
        DocumentReference homespaceRef = db.collection("homespaces").document(homespaceID);

        homespaceRef.update("userList", FieldValue.arrayUnion(user.getUserUID()))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "homespace user list update successful", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "homespace user list update unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
