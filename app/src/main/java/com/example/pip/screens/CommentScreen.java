package com.example.pip.screens;

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
import com.example.pip.Models.CommentModel;
import com.example.pip.R;
import com.example.pip.Models.UserModel;
import com.example.pip.Adapters.CommentAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommentScreen extends AppCompatActivity {

    private TextView setUserNameInCommentSection, setUserPipDataInCommentSection;
    private final DatabaseReference userDataRef = FirebaseDatabase.getInstance().getReference("user").child("UserInfo");
    private ImageView sendBtn, userPhotoInCommentPage;
    private final DatabaseReference userPipDataRef = FirebaseDatabase.getInstance().getReference("user").child("UserPost").child("UserPipData");
    private EditText writeComment;
    private RecyclerView showComment;
    private ArrayList<CommentModel> storeUserReply = new ArrayList<>();
    CommentAdapter showcommentadapter;
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
                    UserModel userModel = snapshot.getValue(UserModel.class);
                    ImageUri = userModel.User_Profile_Image_Uri;
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
                UserModel userModel = snapshot.getValue(UserModel.class);
                storeUserName = userModel.usName;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        showComment.setHasFixedSize(true);
        showcommentadapter = new CommentAdapter(this, storeUserReply);
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
                    UserModel userModel = snapshot.getValue(UserModel.class);
                    Glide.with(CommentScreen.this).load(Uri.parse(userModel.User_Profile_Image_Uri)).into(userPhotoInCommentPage);
                } else {
                    userPhotoInCommentPage.setImageResource(R.drawable.usermodel);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void takeCommentFromUser() {
        sendBtn.setOnClickListener(v -> {
            CommentModel comment = new CommentModel(FirebaseAuth.getInstance().getUid(), writeComment.getText().toString(), ImageUri
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
                        CommentModel comment = ds.getValue(CommentModel.class);
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
