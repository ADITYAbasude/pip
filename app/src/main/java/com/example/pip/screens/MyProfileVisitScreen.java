package com.example.pip.screens;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pip.Adapters.MyPostDataControlsAdapter;
import com.example.pip.R;
import com.example.pip.Models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyProfileVisitScreen extends AppCompatActivity {
    private ImageView setUserImage , location , web , dob;
    private TextView putUserName, putUserBio, putUserLocation, putUserWebsite, putUserDOB, FollowerCount, FollowingCount;
    private final DatabaseReference userDataRef = FirebaseDatabase.getInstance().getReference("user").child("UserInfo").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    private final DatabaseReference userPipDataRef = FirebaseDatabase.getInstance().getReference("user").child("UserPost").child("UserPipData")
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    private ProgressBar proBar;
    private RecyclerView setUser_pip_data;
    private LinearLayout  Following , Follower;
    private ArrayList<UserModel> Store_pip_data = new ArrayList<>();
    MyPostDataControlsAdapter current_user_pip_data_in_profile;
    boolean notifydata = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_visit);
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));


//        ---------id declaration ----------------
        putUserBio = findViewById(R.id.putUserBio);
        putUserDOB = findViewById(R.id.putUserDOB);
        putUserLocation = findViewById(R.id.putUserLocation);
        putUserWebsite = findViewById(R.id.putUserWebsite);
        putUserName = findViewById(R.id.putUserName);
        setUserImage = findViewById(R.id.setUserImage);
        proBar = findViewById(R.id.progressBar3);
        setUser_pip_data = findViewById(R.id.setUser_pip_data);
        FollowerCount = findViewById(R.id.FollowerCount);
        FollowingCount = findViewById(R.id.FollowingCount);
        Following = findViewById(R.id.Following);
        Follower = findViewById(R.id.Follower);
        web = findViewById(R.id.web);
        location = findViewById(R.id.location);
        dob = findViewById(R.id.dob);


        proBar.setVisibility(View.VISIBLE);
        puUserData();
        setUser_data_in_recyclerView();



        ActionBar bar;
        bar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#ffffff"));
        bar.setBackgroundDrawable(colorDrawable);
        assert bar != null;
        bar.setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow =  ContextCompat.getDrawable(this, R.drawable.arrow_back);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_ATOP);
        bar.setHomeAsUpIndicator(upArrow);

//        -----------------Following And Follower Count Program----------------------
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            userDataRef.child("Following").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        FollowingCount.setText(String.valueOf(snapshot.getChildrenCount()));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            userDataRef.child("Follower").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        FollowerCount.setText(String.valueOf(snapshot.getChildrenCount()));
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }


//        -------------------------------------------------------------

        Following.setOnClickListener(v -> {
            FollowingClick();
        });
        Follower.setOnClickListener(v -> {
            FollowingClick();
        });




        proBar.setVisibility(View.GONE);
    }

    private void FollowingClick(){
        Intent intent = new Intent(this , FollowingFollowerTabScreen.class);
        intent.putExtra("usName" , putUserName.getText().toString());
        startActivity(intent);
    }


    private void puUserData() {
        userDataRef.child("EditedData").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    UserModel userModel = snapshot.getValue(UserModel.class);
                    assert userModel != null;
                    putUserBio.setText(userModel.Bio);
                    putUserDOB.setText(userModel.dateOfBirth);
                    putUserWebsite.setText(userModel.Website);
                    putUserLocation.setText(userModel.Location);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        if (putUserLocation.getText().toString() != null) {
            location.setVisibility(View.VISIBLE);
            putUserLocation.setVisibility(View.VISIBLE);
            if (putUserWebsite.getText().toString() != null) {
                web.setVisibility(View.VISIBLE);
                putUserWebsite.setVisibility(View.VISIBLE);
                if (putUserDOB.getText().toString() != null) {
                    dob.setVisibility(View.VISIBLE);
                    putUserDOB.setVisibility(View.VISIBLE);
                }
            } else if (putUserDOB.getText().toString() != null) {
                dob.setVisibility(View.VISIBLE);
                putUserDOB.setVisibility(View.VISIBLE);
            }
        }


        if (putUserWebsite.getText().toString() != null) {
            web.setVisibility(View.VISIBLE);
            putUserWebsite.setVisibility(View.VISIBLE);
            if (putUserLocation.getText().toString() != null) {
                location.setVisibility(View.VISIBLE);
                putUserLocation.setVisibility(View.VISIBLE);
                if (putUserDOB.getText().toString() != null) {
                    dob.setVisibility(View.VISIBLE);
                    putUserDOB.setVisibility(View.VISIBLE);
                }
            }
            else if (putUserDOB.getText().toString() != null) {
                dob.setVisibility(View.VISIBLE);
                putUserDOB.setVisibility(View.VISIBLE);
            }
        }

        if (putUserDOB.getText().toString() != null) {
            dob.setVisibility(View.VISIBLE);
            putUserDOB.setVisibility(View.VISIBLE);
            if (putUserLocation.getText().toString() != null) {
                location.setVisibility(View.VISIBLE);
                putUserLocation.setVisibility(View.VISIBLE);
                if (putUserWebsite.getText().toString() != null) {
                    web.setVisibility(View.VISIBLE);
                    putUserWebsite.setVisibility(View.VISIBLE);
                }
            } else if (putUserWebsite.getText().toString() != null) {
                web.setVisibility(View.VISIBLE);
                putUserWebsite.setVisibility(View.VISIBLE);
            }
        }

        userDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userModel = snapshot.getValue(UserModel.class);
                putUserName.setText(userModel.usName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        userDataRef.child("Profile_Image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    UserModel userModel = snapshot.getValue(UserModel.class);
                    Glide.with(MyProfileVisitScreen.this).load(Uri.parse(userModel.User_Profile_Image_Uri)).into(setUserImage);
                } else {
                    setUserImage.setImageResource(R.drawable.usermodel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void setUser_data_in_recyclerView() {
        userPipDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Store_pip_data.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        UserModel userModel = ds.getValue(UserModel.class);
                        Store_pip_data.add(userModel);

                    }
                    if (notifydata == true) {
                        current_user_pip_data_in_profile.notifyDataSetChanged();
                        notifydata = false;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        setUser_pip_data.setHasFixedSize(true);
        current_user_pip_data_in_profile = new MyPostDataControlsAdapter(this, Store_pip_data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        setUser_pip_data.setLayoutManager(layoutManager);
        setUser_pip_data.setAdapter(current_user_pip_data_in_profile);


    }


}