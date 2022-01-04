package com.example.pip;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class userComment extends AppCompatActivity {

    private TextView setUserNameInCommentSection, setUserPipDataInCommentSection;
    private final DatabaseReference userDataRef = FirebaseDatabase.getInstance().getReference("user").child("UserInfo");
    private ImageView sendBtn, userPhotoInCommentPage;
    private final DatabaseReference userPipDataRef = FirebaseDatabase.getInstance().getReference("user").child("UserPost").child("UserPipData");
    private EditText writeComment;
    private RecyclerView showComment;
    private ArrayList<comment> storeUserReply = new ArrayList<>();
    showCommentAdapter showcommentadapter;
    private ProgressBar progressBarInRecyclerView;
    private String storeUserName, Store_pip_id, u_id, ImageUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usercomment);


//        ---------------------ActionBar hide------------------
        getSupportActionBar().hide();

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.gray_light));

//----------------------------id declare----------------
        setUserNameInCommentSection = findViewById(R.id.setUserNameInCommentSection);
        setUserPipDataInCommentSection = findViewById(R.id.setUserPipDataInCommentSection);
        sendBtn = findViewById(R.id.sendBtn);
        writeComment = findViewById(R.id.writeComment);
        showComment = findViewById(R.id.showComment);
        progressBarInRecyclerView = findViewById(R.id.progressBarInRecyclerView);
        userPhotoInCommentPage = findViewById(R.id.userPhotoInCommentPage);


        Bundle getData = getIntent().getExtras();


        setUserNameInCommentSection.setText(getData.getString("userName"));
        setUserPipDataInCommentSection.setText(getData.getString("pipData"));
        Store_pip_id = getData.getString("pip_id");


        userDataRef.orderByChild("usName").equalTo(setUserNameInCommentSection.getText().toString()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                u_id = snapshot.getRef().getKey();
                setUserImage(u_id);
                setUserCommentToRecycler(u_id);
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

        userDataRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Profile_Image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    User user = snapshot.getValue(User.class);
                    ImageUri = user.User_Profile_Image_Uri;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                ImageUri = null;
            }
        });

        userDataRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                storeUserName = user.usName;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        showComment.setHasFixedSize(true);
        showcommentadapter = new showCommentAdapter(this, storeUserReply);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        showComment.setLayoutManager(layoutManager);
        showComment.setAdapter(showcommentadapter);


        progressBarInRecyclerView.setVisibility(View.VISIBLE);
        takeCommentFromUser();


    }

    private void setUserImage(String uid) {
        userDataRef.child(uid).child("Profile_Image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    Glide.with(userComment.this).load(Uri.parse(user.User_Profile_Image_Uri)).into(userPhotoInCommentPage);
                } else {
                    userPhotoInCommentPage.setImageResource(R.drawable.ic_baseline_account_circle_24);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void takeCommentFromUser() {
        sendBtn.setOnClickListener(v -> {
            comment comment = new comment(FirebaseAuth.getInstance().getUid(), writeComment.getText().toString(), ImageUri
                    , storeUserName);
            userPipDataRef.child(u_id).child(Store_pip_id).child("Comments").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    snapshot.getRef().push().setValue(comment);
                    writeComment.setText("");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });
    }


    private void setUserCommentToRecycler(String uid) {

        progressBarInRecyclerView.setVisibility(View.GONE);

        userPipDataRef.child(uid).child(Store_pip_id).child("Comments").addValueEventListener(new ValueEventListener() {
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


}
