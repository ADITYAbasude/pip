package com.example.pip;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import java.util.List;
import java.util.Locale;

public class User_profile_visit extends AppCompatActivity {
    private ImageView setUserImage;
    private TextView putUserName, putUserBio, putUserLocation, putUserWebsite, putUserDOB;
    private final DatabaseReference userDataRef = FirebaseDatabase.getInstance().getReference("user").child("UserInfo").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    private final DatabaseReference userPipDataRef = FirebaseDatabase.getInstance().getReference("user").child("UserPost").child("UserPipData")
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    private ProgressBar proBar;
    private RecyclerView setUser_pip_data;
    private ArrayList<User> Store_pip_data = new ArrayList<>();
    Current_user_pip_data_in_profile current_user_pip_data_in_profile;
    boolean notifydata = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_visit);
        ActionBar bar = getSupportActionBar();
        bar.hide();
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.gray_light));


//        ---------id declaration ----------------
        putUserBio = findViewById(R.id.putUserBio);
        putUserDOB = findViewById(R.id.putUserDOB);
        putUserLocation = findViewById(R.id.putUserLocation);
        putUserWebsite = findViewById(R.id.putUserWebsite);
        putUserName = findViewById(R.id.putUserName);
        setUserImage = findViewById(R.id.setUserImage);
        proBar = findViewById(R.id.progressBar3);
        setUser_pip_data = findViewById(R.id.setUser_pip_data);


        proBar.setVisibility(View.VISIBLE);
        puUserData();
        setUser_data_in_recyclerView();
        proBar.setVisibility(View.GONE);
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
//        current_user_pip_data_in_profile.


    }


}