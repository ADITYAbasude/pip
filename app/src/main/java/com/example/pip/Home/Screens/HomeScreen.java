package com.example.pip.Home.Screens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pip.Adapters.HomePostAdapter;
import com.example.pip.Models.UserModel;
import com.example.pip.databinding.FragmentHomeBinding;
import com.example.pip.Home.post.PostScreen;
import com.example.pip.screens.StartScreen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;


public class HomeScreen extends Fragment {
    private HomePostAdapter homePageAdapter;
    private final ArrayList<UserModel> storePipData = new ArrayList<>();
    private final ArrayList<String> followerId = new ArrayList<>();
    public final DatabaseReference userPipDataRef = FirebaseDatabase.getInstance().getReference("user")
            .child("UserPost").child("UserPipData");
    public final DatabaseReference userDataRef = FirebaseDatabase.getInstance().getReference("user")
            .child("UserInfo").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
    private boolean notifyData = true;

    private FragmentHomeBinding binding;

    public HomeScreen() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater , container , false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // ----------------------------id decleration ------------------------

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent intent = new Intent(getContext(), StartScreen.class);
            startActivity(intent);
            requireActivity().finish();
        } else {
            getAllFollowerId();
        }

//        ------------------recyclerView setup ----------------------
        binding.pipShow.setHasFixedSize(true);
        homePageAdapter = new HomePostAdapter(getContext(), storePipData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        binding.pipShow.setLayoutManager(layoutManager);
        binding.pipShow.setAdapter(homePageAdapter);


//        ----------click on floating btn for twit --------------

        binding.addYourPost.setOnClickListener(view1 -> {
            Intent userTwitPageOpen = new Intent(getContext(), PostScreen.class);
            startActivity(userTwitPageOpen);
        });
    }


    private void getAllFollowerId() {
        storePipData.clear();
        binding.progressbar.setVisibility(View.VISIBLE);
        userDataRef.child("Following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followerId.clear();
                if (snapshot.exists()) {

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        followerId.add(ds.getKey());
                    }
                } else {
                    binding.errorText.setVisibility(View.VISIBLE);
                    binding.progressbar.setVisibility(View.GONE);
                }
                followerId.add(FirebaseAuth.getInstance().getUid());
                retrieveData();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void retrieveData() {
        storePipData.clear();
        for (int i = 0; i < followerId.size(); i++) {
            userPipDataRef.child(followerId.get(i)).addValueEventListener(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot ds1 : snapshot.getChildren()) {
                            UserModel userModel1 = ds1.getValue(UserModel.class);
                            storePipData.add(userModel1);
                        }
                        if (notifyData) {
                            homePageAdapter.notifyDataSetChanged();
                            notifyData = false;
                        }
                        binding.progressbar.setVisibility(View.GONE);
                        binding.errorText.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}


