package com.example.pip.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pip.Adapters.HomePostAdapter;
import com.example.pip.R;
import com.example.pip.Models.UserModel;
import com.example.pip.screens.post.PostScreen;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HomePostScreen extends Fragment {
    private FloatingActionButton addTwitBtn;
    private HomePostAdapter homePageAdapter;
    private RecyclerView pipShow;
    private ArrayList<UserModel> storePipData = new ArrayList<>();
    private ArrayList<String> followerId = new ArrayList<>();
    private ProgressBar progessbar;
    public final DatabaseReference userPipDataRef = FirebaseDatabase.getInstance().getReference("user").child("UserPost").child("UserPipData");
    public final DatabaseReference userDataRef = FirebaseDatabase.getInstance().getReference("user").child("UserInfo").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    private boolean notifyData = true;
    private TextView errorText;

    public HomePostScreen() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // ----------------------------id decleration ------------------------

        addTwitBtn = view.findViewById(R.id.floatingActionButtonforTwit);
        pipShow = view.findViewById(R.id.pipShow);
        progessbar = view.findViewById(R.id.progressbar);
        errorText = view.findViewById(R.id.errorText);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent intent = new Intent(getContext(), StartScreen.class);
            startActivity(intent);
            getActivity().finish();
        } else {
            getAllFollowerId();
        }

//        ------------------recyclerView setup ----------------------
        pipShow.setHasFixedSize(true);
        homePageAdapter = new HomePostAdapter(getContext(), storePipData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        pipShow.setLayoutManager(layoutManager);
        pipShow.setAdapter(homePageAdapter);


//        ----------click on floating btn for twit --------------

        addTwitBtn.setOnClickListener(view1 -> {
            Intent userTwitPageOpen = new Intent(getContext(), PostScreen.class);
            startActivity(userTwitPageOpen);

        });


    }


    private void getAllFollowerId() {
        storePipData.clear();
        progessbar.setVisibility(View.VISIBLE);
        userDataRef.child("Following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followerId.clear();
                if (snapshot.exists()) {

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        followerId.add(ds.getKey());
                    }
                } else {
                    errorText.setVisibility(View.VISIBLE);
                    progessbar.setVisibility(View.GONE);
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
                        progessbar.setVisibility(View.GONE);
                        errorText.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}


