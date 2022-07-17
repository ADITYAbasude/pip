package com.example.pip.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pip.R;
import com.example.pip.Adapters.FollowingAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FollowingFragment extends Fragment {

    private RecyclerView showFollowingUser;
    private List<String> storeFollowingId = new ArrayList<>();
    private DatabaseReference userDataRef = FirebaseDatabase.getInstance().getReference("user").child("UserInfo")
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Following");
    private FollowingAdapter followingAdapter;
    private String userName;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.following_fragment_activity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        showFollowingUser = view.findViewById(R.id.showFollowingUser);

        showFollowingUser.setHasFixedSize(true);
        followingAdapter = new FollowingAdapter(getContext() , storeFollowingId);
        showFollowingUser.setLayoutManager(new LinearLayoutManager(getContext()));
        showFollowingUser.setAdapter(followingAdapter);


        Toast.makeText(getContext(), "Hello", Toast.LENGTH_SHORT).show();
        userDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                storeFollowingId.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    storeFollowingId.add(ds.getKey());
                }
                followingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}