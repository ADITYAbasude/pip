package com.example.pip;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class pip_view extends AppCompatActivity {
    private String pip_id, imageUri, username, uid;
    private final DatabaseReference userPipDataRef = FirebaseDatabase.getInstance().getReference("user").child("UserPost").child("UserPipData");
    private final DatabaseReference userDataRef = FirebaseDatabase.getInstance().getReference("user").child("UserInfo");
    private ImageView setUserImage, heart3, comment3, shearing , setUserPipPhoto;
    private TextView setUsername, pipData, heartCount3, pipDateTime, commentCount;
    private boolean like_dislike = false;
    showCommentAdapter showcommentadapter;
    private ArrayList<comment> storeUserReply = new ArrayList<>();
    private RecyclerView settingUserPipData;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pip_view);

        ActionBar bar;
        bar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#ffffff"));
        bar.setBackgroundDrawable(colorDrawable);
        bar.setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow =  ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back_ios_24);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_ATOP);
        bar.setHomeAsUpIndicator(upArrow);
        bar.setElevation(5);


        Bundle bundle = getIntent().getExtras();
        pip_id = String.valueOf(bundle.get("pip_id"));
        username = String.valueOf(bundle.get("name"));


        setUserImage = findViewById(R.id.setUserPhoto);
        setUsername = findViewById(R.id.username);
        pipData = findViewById(R.id.pipData);
        heart3 = findViewById(R.id.heart3);
        heartCount3 = findViewById(R.id.heartCount3);
        pipDateTime = findViewById(R.id.pipDateTime);
        comment3 = findViewById(R.id.comment3);
        shearing = findViewById(R.id.shearing);
        settingUserPipData = findViewById(R.id.settingUserPipData);
        commentCount = findViewById(R.id.commentCount3);
        setUserPipPhoto = findViewById(R.id.setUserPipPhoto);

        userDataRef.orderByChild("usName").equalTo(username).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                uid = snapshot.getKey();
                takePipData(uid);
                setUserImagefuc(uid);
                pip_like_data_store(uid);
                likeStatus(uid);
                takeComment(uid);
                countComment(uid);

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


        settingUserPipData.setHasFixedSize(true);
        showcommentadapter = new showCommentAdapter(this, storeUserReply);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        settingUserPipData.setLayoutManager(layoutManager);
        settingUserPipData.setAdapter(showcommentadapter);


        comment3.setOnClickListener(view -> {
            Intent moveCommentPage = new Intent(this, userComment.class);
            moveCommentPage.putExtra("userName", setUsername.getText().toString());
            moveCommentPage.putExtra("pipData", pipData.getText().toString());
            moveCommentPage.putExtra("pip_id", pip_id);
            startActivity(moveCommentPage);

        });

        shearing.setOnClickListener(view -> {
            Intent share = new Intent();
            share.setAction(Intent.ACTION_SEND);
            share.putExtra(Intent.EXTRA_TEXT, pipData.getText().toString());
            share.setType("text/plain");
            Intent shareIntent = Intent.createChooser(share, "Pip Post");
            startActivity(shareIntent);
        });


    }


    private void takePipData(String uid) {
        userPipDataRef.child(uid).child(pip_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                setUsername.setText(user.pipuserName);
                pipData.setText(user.pipPostData);
                pipDateTime.setText(user.date);
                snapshot.getRef().child("ImageUriFromDatabase").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            pipDataImageModel imageModel = snapshot.getValue(pipDataImageModel.class);
                            setUserImage.setVisibility(View.VISIBLE);
                            Glide.with(pip_view.this).load(Uri.parse(imageModel.pipImageData)).into(setUserPipPhoto);
                        } else {
                            setUserPipPhoto.setImageResource(R.drawable.ic_baseline_home_24);
                            setUserPipPhoto.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setUserImagefuc(String uid) {
        userDataRef.child(uid).child("Profile_Image").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    Glide.with(pip_view.this).load(user.User_Profile_Image_Uri).into(setUserImage);
                } else {
                    setUserImage.setImageResource(R.drawable.ic_baseline_account_circle_24);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void pip_like_data_store(String uid) {
        heart3.setOnClickListener(v -> {
            like_dislike = true;
            userPipDataRef.child(uid).child(pip_id).child("Likes").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (like_dislike) {
                        if (snapshot.hasChild((FirebaseAuth.getInstance().getCurrentUser().getUid()))) {
                            heart3.setImageResource(R.drawable.heart);
                            snapshot.getRef().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(null);
                            like_dislike = false;
                        } else {
                            mp = MediaPlayer.create(pip_view.this, R.raw.heart_click_sound);
                            mp.setOnPreparedListener(mp1 -> {
                                mp.start();
                            });
                            heart3.setImageResource(R.drawable.heartred);
                            snapshot.getRef().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
                            like_dislike = false;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        });

    }

    private void likeStatus(String uid) {

        userPipDataRef.child(uid).child(pip_id).child("Likes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if ((snapshot.hasChild(FirebaseAuth.getInstance().getUid()))) {
                    heart3.setImageResource(R.drawable.heartred);
                    heartCount3.setText(Integer.toString((int) snapshot.getChildrenCount()));

                } else {
                    heart3.setImageResource(R.drawable.heart);
                    heartCount3.setText(Integer.toString((int) snapshot.getChildrenCount()));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void takeComment(String uid) {
        userPipDataRef.child(uid).child(pip_id).child("Comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    storeUserReply.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        comment comment = ds.getValue(comment.class);
                        storeUserReply.add(comment);
                    }
                    showcommentadapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void countComment(String uid) {
        userPipDataRef.child(uid).child(pip_id).child("Comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    commentCount.setText(String.valueOf((int) snapshot.getChildrenCount()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}