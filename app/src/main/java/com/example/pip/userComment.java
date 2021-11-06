package com.example.pip;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class userComment extends AppCompatActivity {

    private TextView setUserNameInCommentSection, setUserPipDataInCommentSection;
    private final DatabaseReference takeRef = FirebaseDatabase.getInstance().getReference("user").child("All-User-pip-Data");
    private ImageView sendBtn;

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

        Bundle getData = getIntent().getExtras();


        setUserNameInCommentSection.setMovementMethod(new ScrollingMovementMethod());
        setUserNameInCommentSection.setText(getData.getString("userName"));
        setUserPipDataInCommentSection.setText(getData.getString("pipData"));


        takeCommentFromUser();

    }


    private void takeCommentFromUser() {
        sendBtn.setOnClickListener(view -> {

            User user = new User(setUserNameInCommentSection.getText().toString());
            takeRef.orderByChild("pipPostData").equalTo(setUserPipDataInCommentSection.getText().toString());
            takeRef.child("comments").push().setValue(user);

        });
    }


}
