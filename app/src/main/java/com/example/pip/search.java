package com.example.pip;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class search extends Fragment {
    private EditText searchView;
    private RecyclerView storePipUerName;
    private final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private SearchAdapter gup;
    private ProgressBar progressBar;
    private boolean followOrNot;
    private TextView userNotFound;
    private ArrayList<User> Store_User_Info = new ArrayList<>();
    private ArrayList<User> Store_User_Img = new ArrayList<>();
    Uri img_uri;

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
        storePipUerName = view.findViewById(R.id.serachresult);
        progressBar = view.findViewById(R.id.progressBar);
        userNotFound = view.findViewById(R.id.userNotFound);


        gup = new SearchAdapter(getContext(), Store_User_Info);
        storePipUerName.setLayoutManager(new LinearLayoutManager(getContext()));

        storePipUerName.setAdapter(gup);

//        ----------------datasnapshot from firebase---------------
        takeUserNameFromFirebase();


    }


    private void takeUserNameFromFirebase() {


        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                progressBar.setVisibility(View.VISIBLE);
                ref.child("user").child("UserInfo").orderByChild("usName").startAt(searchView.getText().toString()).endAt("\uf8ff")
                        .addValueEventListener(new ValueEventListener() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    Store_User_Info.clear();
                                    Store_User_Img.clear();
                                    for (DataSnapshot ds : snapshot.getChildren()) {
                                        User user = ds.getValue(User.class);
                                        Store_User_Info.add(user);
                                        progressBar.setVisibility(View.GONE);
                                        userNotFound.setVisibility(View.GONE);
                                    }
                                    gup.notifyDataSetChanged();
                                } else {
                                    userNotFound.setVisibility(View.VISIBLE);
                                    userNotFound.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                }

                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }
}