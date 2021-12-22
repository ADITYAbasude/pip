package com.example.pip;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.contentcapture.DataShareRequest;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
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

//        takePipDataFromFirebase();
//
//        ----------click on floating btn for twit --------------

        addTwitBtn.setOnClickListener(view1 -> {
            Intent userTwitPageOpen = new Intent(getContext(), twittingpageUser.class);
            startActivity(userTwitPageOpen);

        });


    }


//    private void takePipDataFromFirebase() {
//        progessbar.setVisibility(View.VISIBLE);
//        FirebaseDatabase.getInstance().getReference("user").child("UserPost").child("UserPipData")
//                .addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                        storePipData.clear();
//                        for (DataSnapshot ds : snapshot.getChildren()) {
//                            ds.getRef().addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot snapshot2) {
//                                    for (DataSnapshot ds2 : snapshot2.getChildren()) {
//                                        User user = ds2.getValue(User.class);
//                                        storePipData.add(user);
//                                    }
//                                    homePageAdapter.notifyDataSetChanged();
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError error) {
//
//                                }
//                            });
//                            Toast.makeText(getContext(), snapshot.getRef().getKey(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                    }
//
//                    @Override
//                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//                    }
//
//                    @Override
//                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//        progessbar.setVisibility(View.GONE);
//    }


}


