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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "HomeFragment";

    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private Button mSignOut;
    private EditText mAddMemberEditText;
    private String homespaceID;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private DocumentSnapshot mLastQueriedDocument;

    private ArrayList<User> mUserList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, null);
        mSignOut = v.findViewById(R.id.signOutButton);
        mSignOut.setOnClickListener(this);

        v.findViewById(R.id.addMemberButton).setOnClickListener(this);
        mAddMemberEditText = v.findViewById(R.id.addMemberEditText);

        setupFirebaseListener();

        mUserList = new ArrayList<>();

        mRecyclerView = v.findViewById(R.id.homespaceMemberListRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mAdapter = new UserItemRecyclerViewAdapter(mUserList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout = v.findViewById(R.id.homeFragmentUserListSwipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        getHomespaceID();
        getUsers();

        return v;
    }

    @Override
    public void onRefresh() {
        getUsers();
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private void getHomespaceID(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userUID = FirebaseAuth.getInstance().getUid();

        // first get homespaceID from current user
        CollectionReference usersRef = db.collection("users");

        Query currentUserQuery = usersRef.whereEqualTo("userUID", userUID);
        currentUserQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        User user = document.toObject(User.class);
                        mUserList.add(user);
                    }
                } else {
                    Toast.makeText(getActivity(), "Error getting homespaceID", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //homespaceID = "";
        if (!mUserList.isEmpty())
            homespaceID = mUserList.get(0).getHomespaceID();
        mUserList.clear();
    }

    private void getUsers() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // second get the users with the same homespaceID
        CollectionReference usersRef = db.collection("users");

        Query usersQuery = null;
        if (mLastQueriedDocument != null) {
            usersQuery = usersRef.whereEqualTo("homespaceID", homespaceID)
                    .startAfter(mLastQueriedDocument); // for no duplicates on refresh
        } else {
            usersQuery = usersRef.whereEqualTo("homespaceID", homespaceID);
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

    private void setupFirebaseListener() {
        Log.d(TAG, "setupFirebaseListener: setting up the auth state listener");
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged: signed_in " + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                    Toast.makeText(getActivity(), "Signed Out", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener);
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
                String username = mAddMemberEditText.getText().toString().trim();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                // TODO: Add members

                break;
            }
        }
    }
}
