package com.example.pip;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.progressindicator.BaseProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Visit_Another_profile extends AppCompatActivity {

    private ImageView setUserImageInAnotherProfile, webImage, dobImage, locationImage;
    private TextView putUserNameInAnotherProfile, putUserBioInAnotherProfile,
            putUserLocationInAnotherProfile, putUserWebsiteInAnotherProfile, putUserDOBInAnotherProfile,
            fingCount, fwerCount;
    private final DatabaseReference userDataRef = FirebaseDatabase.getInstance().getReference("user").child("UserInfo");
    private final DatabaseReference userPipDataRef = FirebaseDatabase.getInstance().getReference("user").child("UserPost").child("UserPipData");
    private RecyclerView setAnotherUserPipData;
    private ArrayList<User> Store_pip_dataOfAnotherProfile = new ArrayList<>();
    private Button DoFollow_Unfollow;
    private String takeUserId, u_id;
    AnotherProfileAdapter anotherProfileAdapter;
    private Boolean checkFollowOrNot = false, notifyData = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_another_profile);
        getSupportActionBar().hide();

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.gray_light));

        //        ---------id declaration ----------------
        putUserBioInAnotherProfile = findViewById(R.id.setUserBioInAnotherProfile);
        putUserDOBInAnotherProfile = findViewById(R.id.setUserDOBInAnotherProfile);
        putUserLocationInAnotherProfile = findViewById(R.id.setUserLocationInAnotherProfile);
        putUserWebsiteInAnotherProfile = findViewById(R.id.setUserWebsiteInAnotherProfile);
        putUserNameInAnotherProfile = findViewById(R.id.setUserNameInAnotherProfile);
        setUserImageInAnotherProfile = findViewById(R.id.setUserImageInAnotherProfile);
        setAnotherUserPipData = findViewById(R.id.setAnotherUserPipData);
        DoFollow_Unfollow = findViewById(R.id.DoFollowOrUnfollow);
        fingCount = findViewById(R.id.fingCount);
        fwerCount = findViewById(R.id.fwerCount);
        webImage = findViewById(R.id.webImage);
        locationImage = findViewById(R.id.locationImage);
        dobImage = findViewById(R.id.dobImage);


        Bundle b = getIntent().getExtras();
        u_id = b.getString("Uid");


        userCanDoFollowOrUnfollow();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference("user").child("UserInfo").orderByChild("usName").equalTo(u_id)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        takeUserId = snapshot.getKey();
                        putUserData(takeUserId);
                        setUser_data_in_recyclerView(takeUserId);
                        checkStatus(takeUserId);


                        userDataRef.child(takeUserId).child("Following").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                fingCount.setText(String.valueOf(snapshot.getChildrenCount()));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        userDataRef.child(takeUserId).child("Follower").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                fwerCount.setText(String.valueOf(snapshot.getChildrenCount()));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    private void putUserData(String takeUserId) {
        userDataRef.child(takeUserId).child("EditedData").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    assert user != null;
                    putUserBioInAnotherProfile.setText(user.Bio);
                    putUserDOBInAnotherProfile.setText(user.dateOfBirth);
                    putUserWebsiteInAnotherProfile.setText(user.Website);
                    putUserLocationInAnotherProfile.setText(user.Location);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        if (putUserLocationInAnotherProfile.getText().toString() != null) {
            locationImage.setVisibility(View.VISIBLE);
            putUserLocationInAnotherProfile.setVisibility(View.VISIBLE);
            if (putUserWebsiteInAnotherProfile.getText().toString() != null) {
                webImage.setVisibility(View.VISIBLE);
                putUserWebsiteInAnotherProfile.setVisibility(View.VISIBLE);
                if (putUserDOBInAnotherProfile.getText().toString() != null) {
                    dobImage.setVisibility(View.VISIBLE);
                    putUserDOBInAnotherProfile.setVisibility(View.VISIBLE);
                }
            } else if (putUserDOBInAnotherProfile.getText().toString() != null) {
                dobImage.setVisibility(View.VISIBLE);
                putUserDOBInAnotherProfile.setVisibility(View.VISIBLE);
            }
        }


        if (putUserWebsiteInAnotherProfile.getText().toString() != null) {
            webImage.setVisibility(View.VISIBLE);
            putUserWebsiteInAnotherProfile.setVisibility(View.VISIBLE);
            if (putUserLocationInAnotherProfile.getText().toString() != null) {
                locationImage.setVisibility(View.VISIBLE);
                putUserLocationInAnotherProfile.setVisibility(View.VISIBLE);
                if (putUserDOBInAnotherProfile.getText().toString() != null) {
                    dobImage.setVisibility(View.VISIBLE);
                    putUserDOBInAnotherProfile.setVisibility(View.VISIBLE);
                }
            }
            else if (putUserDOBInAnotherProfile.getText().toString() != null) {
                dobImage.setVisibility(View.VISIBLE);
                putUserDOBInAnotherProfile.setVisibility(View.VISIBLE);
            }
        }

        if (putUserDOBInAnotherProfile.getText().toString() != null) {
            dobImage.setVisibility(View.VISIBLE);
            putUserDOBInAnotherProfile.setVisibility(View.VISIBLE);
            if (putUserLocationInAnotherProfile.getText().toString() != null) {
                locationImage.setVisibility(View.VISIBLE);
                putUserLocationInAnotherProfile.setVisibility(View.VISIBLE);
                if (putUserWebsiteInAnotherProfile.getText().toString() != null) {
                    webImage.setVisibility(View.VISIBLE);
                    putUserWebsiteInAnotherProfile.setVisibility(View.VISIBLE);
                }
            } else if (putUserWebsiteInAnotherProfile.getText().toString() != null) {
                webImage.setVisibility(View.VISIBLE);
                putUserWebsiteInAnotherProfile.setVisibility(View.VISIBLE);
            }
        }





        userDataRef.child(takeUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                putUserNameInAnotherProfile.setText(user.usName);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        userDataRef.child(takeUserId).child("Profile_Image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    Glide.with(Visit_Another_profile.this).load(Uri.parse(user.User_Profile_Image_Uri)).into(setUserImageInAnotherProfile);
                } else {
                    setUserImageInAnotherProfile.setImageResource(R.drawable.ic_baseline_account_circle_24);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void setUser_data_in_recyclerView(String takeUserUid) {
        setAnotherUserPipData.setHasFixedSize(true);

        anotherProfileAdapter = new AnotherProfileAdapter(this, Store_pip_dataOfAnotherProfile, takeUserUid);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        setAnotherUserPipData.setLayoutManager(layoutManager);
        setAnotherUserPipData.setAdapter(anotherProfileAdapter);

        userPipDataRef.child(takeUserUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Store_pip_dataOfAnotherProfile.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        User user = ds.getValue(User.class);
                        Store_pip_dataOfAnotherProfile.add(user);

                    }
                    if (notifyData) {
                        anotherProfileAdapter.notifyDataSetChanged();
                        notifyData = false;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void userCanDoFollowOrUnfollow() {
        DoFollow_Unfollow.setOnClickListener(v -> {
            checkFollowOrNot = true;
            userDataRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Following").addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (checkFollowOrNot) {
                        if (snapshot.hasChild(takeUserId)) {
                            snapshot.getRef().child(takeUserId).setValue(null);
                            DoFollow_Unfollow.setText("Follow");
                            userDataRef.child(takeUserId).child("Follower").child(FirebaseAuth.getInstance()
                                    .getCurrentUser().getUid()).setValue(null);
                            checkFollowOrNot = false;
                        } else {
                            snapshot.getRef().child(takeUserId).setValue(true);
                            DoFollow_Unfollow.setText("Following");
                            userDataRef.child(takeUserId).child("Follower").child(FirebaseAuth.getInstance()
                                    .getCurrentUser().getUid()).setValue(true);
                            checkFollowOrNot = false;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });
    }

    private void checkStatus(String takeUserId) {
        userDataRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Following").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(takeUserId)) {
                    DoFollow_Unfollow.setText("Following");
                } else {
                    DoFollow_Unfollow.setText("Follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}