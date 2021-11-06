package com.example.pip;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class twittingpageUser extends AppCompatActivity {
    private EditText takingPipFromUser;
    private Button submmitPip;
    private String pipStoreData, storeUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twittingpage_user);


//        ---------id declaration-------------
        takingPipFromUser = findViewById(R.id.takingPipFromUser);
        submmitPip = findViewById(R.id.pipPost);


        ActionBar ABsupport = getSupportActionBar();
        ABsupport.hide();


//        ----------------getText Of pip and upload on the Database-----------
        pipStoreData = takingPipFromUser.toString();
        getUserNameFromDatabase();
        getTextAndUploadToDatavbse();


    }


    private void getTextAndUploadToDatavbse() {
        submmitPip.setOnClickListener(view -> {
            DatabaseReference puskKey = FirebaseDatabase.getInstance().getReference("user").push();
            String getPipData = takingPipFromUser.getText().toString().trim();
            User user = new User(getPipData, storeUserName, "0", "0");
            FirebaseDatabase.getInstance().getReference("user").child("All-User-Pip-Data")
                    .child(puskKey.getKey())
                    .setValue(user).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    takingPipFromUser.setText("");
                } else {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }
            });

            User user1 = new User(getPipData, storeUserName, "0", "0");
            FirebaseDatabase.getInstance().getReference("user").child("UserPost").child("UserPipData")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(puskKey.getKey()).setValue(user1);
        });
    }


    private void getUserNameFromDatabase() {
        FirebaseDatabase.getInstance().getReference("user").child("UserInfo")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user2 = snapshot.getValue(User.class);
                        storeUserName = user2.usName;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

}