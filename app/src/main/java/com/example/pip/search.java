package com.example.pip;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class search extends Fragment {
    EditText searchView;
    ListView storePipUerName;
    ArrayList<User> StoreUserName = new ArrayList<>();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    gitUserPip gup;
    ProgressBar progressBar;
    public search() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        -------------------id declaration--------------------------
        searchView = view.findViewById(R.id.serachView);
        storePipUerName = view.findViewById(R.id.searchresult);
        progressBar = view.findViewById(R.id.progressBar);

        gup = new gitUserPip(getContext() ,R.layout.pipusername, StoreUserName);
        storePipUerName.setAdapter(gup);

//        ----------------datasnapshot from firebase---------------
        takeUserNameFromFirebase();
    }



    void takeUserNameFromFirebase(){
        progressBar.setVisibility(View.VISIBLE);
        ref.child("user").child("UserInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    StoreUserName.add(user);
                    progressBar.setVisibility(View.GONE);
                }
                gup.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}