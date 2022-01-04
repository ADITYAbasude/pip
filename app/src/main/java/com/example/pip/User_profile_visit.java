package com.example.pip;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.mbms.MbmsErrors;
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
import androidx.recyclerview.widget.SnapHelper;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class User_profile_visit extends AppCompatActivity {
    private ImageView setUserImage , location , web , dob;
    private TextView putUserName, putUserBio, putUserLocation, putUserWebsite, putUserDOB, FollowerCount, FollowingCount;
    private final DatabaseReference userDataRef = FirebaseDatabase.getInstance().getReference("user").child("UserInfo").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    private final DatabaseReference userPipDataRef = FirebaseDatabase.getInstance().getReference("user").child("UserPost").child("UserPipData")
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    private ProgressBar proBar;
    private RecyclerView setUser_pip_data;
    private LinearLayout  Following , Follower;
    private ArrayList<User> Store_pip_data = new ArrayList<>();
    Current_user_pip_data_in_profile current_user_pip_data_in_profile;
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
        bar.setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow =  ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back_ios_24);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_ATOP);
        bar.setHomeAsUpIndicator(upArrow);
        bar.setElevation(5);

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
        Intent intent = new Intent(this , Following_Follower_Activity.class);
        intent.putExtra("usName" , putUserName.getText().toString());
        startActivity(intent);
    }


    private void puUserData() {
        userDataRef.child("EditedData").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    assert user != null;
                    putUserBio.setText(user.Bio);
                    putUserDOB.setText(user.dateOfBirth);
                    putUserWebsite.setText(user.Website);
                    putUserLocation.setText(user.Location);
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
                User user = snapshot.getValue(User.class);
                putUserName.setText(user.usName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        userDataRef.child("Profile_Image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    Glide.with(User_profile_visit.this).load(Uri.parse(user.User_Profile_Image_Uri)).into(setUserImage);
                } else {
                    setUserImage.setImageResource(R.drawable.ic_baseline_account_circle_24);
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
                        User user = ds.getValue(User.class);
                        Store_pip_data.add(user);

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
        current_user_pip_data_in_profile = new Current_user_pip_data_in_profile(this, Store_pip_data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        setUser_pip_data.setLayoutManager(layoutManager);
        setUser_pip_data.setAdapter(current_user_pip_data_in_profile);


    }


}