package com.example.pip.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class FollowerFragment extends Fragment {
    private RecyclerView showFollowingUser;
    private List<String> storeFollowingId = new ArrayList<>();
    private DatabaseReference userDataRef = FirebaseDatabase.getInstance().getReference("user").child("UserInfo")
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Follower");
    FollowingAdapter followingAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_follower_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        showFollowingUser = view.findViewById(R.id.followersShow);


        showFollowingUser.setHasFixedSize(true);
        followingAdapter = new FollowingAdapter(getContext() , storeFollowingId);
        showFollowingUser.setLayoutManager(new LinearLayoutManager(getContext()));
        showFollowingUser.setAdapter(followingAdapter);





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