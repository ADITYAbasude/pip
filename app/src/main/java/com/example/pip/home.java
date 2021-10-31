package com.example.pip;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class home extends Fragment {
    private FloatingActionButton addTwitBtn;
//    getAllPipPost gapp;
    RecyclerView reView;
    ArrayList<User> storePipData = new ArrayList<>();
    ProgressBar progessbar;
    DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();
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
//        reView = view.findViewById(R.id.pipPostHere);
//        progessbar = view.findViewById(R.id.progressbar);



//        ------------------recyclerView setup ----------------------
//        reView.setHasFixedSize(true);
//        gapp = new getAllPipPost(getContext(), storePipData);
//        reView.setLayoutManager(new LinearLayoutManager(getContext()));
//        reView.setAdapter(gapp);/

//        takePipDataFromFirebase();

//        ----------click on floating btn for twit --------------

        addTwitBtn.setOnClickListener(view1 -> {
            Intent userTwitPageOpen = new Intent(getContext() , twittingpageUser.class);
            startActivity(userTwitPageOpen);
        });



    }



//    public void takePipDataFromFirebase(){
//        progessbar.setVisibility(View.VISIBLE);
//        dataRef.child("user").child("PipPostData").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .push().addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot ds : snapshot.getChildren()){
//                    User user = ds.getValue(User.class);
//                    storePipData.add(user);
//                }
//                gapp.notifyDataSetChanged();
//                progessbar.setVisibility(View.GONE);
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}