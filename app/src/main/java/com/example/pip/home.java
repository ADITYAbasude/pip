package com.example.pip;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class home extends Fragment {
    private FloatingActionButton addTwitBtn;
    private homePagePipAdapter homePageAdapter;
    private RecyclerView pipShow;
    private static ArrayList<User> storePipData = new ArrayList<>();
    private ProgressBar progessbar;


    public home() {
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


//        ------------------recyclerView setup ----------------------
        pipShow.setHasFixedSize(true);
        homePageAdapter = new homePagePipAdapter(getContext(), storePipData);
        pipShow.setLayoutManager(new LinearLayoutManager(getContext()));
        pipShow.setAdapter(homePageAdapter);

        takePipDataFromFirebase();
//
//        ----------click on floating btn for twit --------------

        addTwitBtn.setOnClickListener(view1 -> {
            Intent userTwitPageOpen = new Intent(getContext(), twittingpageUser.class);
            startActivity(userTwitPageOpen);
        });


    }

    private void takePipDataFromFirebase() {
        progessbar.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference("user").child("All-User-Pip-Data")
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        storePipData.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            User user = ds.getValue(User.class);
                            storePipData.add(user);
                        }
                        homePageAdapter.notifyDataSetChanged();
                        progessbar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }




}


