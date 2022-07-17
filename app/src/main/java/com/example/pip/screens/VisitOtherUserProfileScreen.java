package com.example.pip.screens;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pip.Adapters.AnotherUserPostAdaptor;
import com.example.pip.R;
import com.example.pip.Models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VisitOtherUserProfileScreen extends AppCompatActivity {

    private ImageView setUserImageInAnotherProfile, webImage, dobImage, locationImage;
    private TextView putUserNameInAnotherProfile, putUserBioInAnotherProfile,
            putUserLocationInAnotherProfile, putUserWebsiteInAnotherProfile, putUserDOBInAnotherProfile,
            fingCount, fwerCount;
    private final DatabaseReference userDataRef = FirebaseDatabase.getInstance().getReference("user").child("UserInfo");
    private final DatabaseReference userPipDataRef = FirebaseDatabase.getInstance().getReference("user").child("UserPost").child("UserPipData");
    private RecyclerView setAnotherUserPipData;
    private ArrayList<UserModel> Store_pip_dataOfAnotherProfile = new ArrayList<>();
    private Button DoFollow_Unfollow;
    private String takeUserId, u_id;
    AnotherUserPostAdaptor anotherUserPostAdaptor;
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
                    UserModel userModel = snapshot.getValue(UserModel.class);
                    assert userModel != null;
                    putUserBioInAnotherProfile.setText(userModel.Bio);
                    putUserDOBInAnotherProfile.setText(userModel.dateOfBirth);
                    putUserWebsiteInAnotherProfile.setText(userModel.Website);
                    putUserLocationInAnotherProfile.setText(userModel.Location);
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
                UserModel userModel = snapshot.getValue(UserModel.class);
                putUserNameInAnotherProfile.setText(userModel.usName);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        userDataRef.child(takeUserId).child("Profile_Image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    UserModel userModel = snapshot.getValue(UserModel.class);
                    Glide.with(VisitOtherUserProfileScreen.this).load(Uri.parse(userModel.User_Profile_Image_Uri)).into(setUserImageInAnotherProfile);
                } else {
                    setUserImageInAnotherProfile.setImageResource(R.drawable.usermodel);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void setUser_data_in_recyclerView(String takeUserUid) {
        setAnotherUserPipData.setHasFixedSize(true);

        anotherUserPostAdaptor = new AnotherUserPostAdaptor(this, Store_pip_dataOfAnotherProfile, takeUserUid);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        setAnotherUserPipData.setLayoutManager(layoutManager);
        setAnotherUserPipData.setAdapter(anotherUserPostAdaptor);

        userPipDataRef.child(takeUserUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Store_pip_dataOfAnotherProfile.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        UserModel userModel = ds.getValue(UserModel.class);
                        Store_pip_dataOfAnotherProfile.add(userModel);

                    }
                    if (notifyData) {
                        anotherUserPostAdaptor.notifyDataSetChanged();
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