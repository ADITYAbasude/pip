package com.example.pip;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private DatabaseReference takeRef = FirebaseDatabase.getInstance().getReference("user").child("All-User-Pip-Data");
    private ImageView sendBtn;
    private EditText writeComment;
    private RecyclerView showComment;
    private ArrayList<User> storeUserReply = new ArrayList<>();
    showCommentAdapter showcommentadapter;
    private ProgressBar progressBarInRecyclerView;
    private String usernamesertext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usercomment);
//        ---------------------ActionBar hide------------------
        getSupportActionBar().hide();

//----------------------------id declare----------------
        setUserNameInCommentSection = findViewById(R.id.setUserNameInCommentSection);
        setUserPipDataInCommentSection = findViewById(R.id.setUserPipDataInCommentSection);
        sendBtn = findViewById(R.id.sendBtn);
        writeComment = findViewById(R.id.writeComment);
        showComment = findViewById(R.id.showComment);
        progressBarInRecyclerView = findViewById(R.id.progressBarInRecyclerView);

        Bundle getData = getIntent().getExtras();


        setUserNameInCommentSection.setText(getData.getString("userName"));
        setUserPipDataInCommentSection.setText(getData.getString("pipData"));

        showComment.setHasFixedSize(true);
        showcommentadapter = new showCommentAdapter(this, storeUserReply);
        showComment.setLayoutManager(new LinearLayoutManager(this));
        showComment.setAdapter(showcommentadapter);


        setCommentFromUser();

        getCommentFromFirebase();
        getName();


    }


    private void setCommentFromUser() {
        sendBtn.setOnClickListener(view -> takeRef.orderByChild("pipPostData").equalTo(setUserPipDataInCommentSection.getText().toString())
                .addChildEventListener(new ChildEventListener() {

                    User user = new User(writeComment.getText().toString(), usernamesertext);

                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        snapshot.getRef().child("comment").push().setValue(user);
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
                }));
    }


    private void getCommentFromFirebase() {
        progressBarInRecyclerView.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference("user").child("All-User-Pip-Data").orderByChild("pipPostData").equalTo(setUserPipDataInCommentSection.getText().toString())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if (snapshot.exists()) {
                            snapshot.getRef().child("comment").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    storeUserReply.clear();
                                    for (DataSnapshot ds : snapshot.getChildren()) {
                                        User user = ds.getValue(User.class);
                                        storeUserReply.add(user);
                                    }
                                    showcommentadapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
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
        progressBarInRecyclerView.setVisibility(View.GONE);
    }


    public void getName() {
        FirebaseDatabase.getInstance().getReference("user").child("UserInfo").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User userprofile = snapshot.getValue(User.class);
                        usernamesertext = userprofile.usName;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}
