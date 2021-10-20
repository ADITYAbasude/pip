package com.example.pip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class twittingpageUser extends AppCompatActivity {
    EditText takingPipFromUser;
    TextView closebtn;
    Button submmitPip;
    String pipstoreData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twittingpage_user);


//        ---------id declaration-------------
        takingPipFromUser = findViewById(R.id.takingPipFromUser);
        closebtn = findViewById(R.id.closeBtn);
        submmitPip = findViewById(R.id.pipPost);


        ActionBar ABsupport = getSupportActionBar();
        ABsupport.hide();

//        ---------move to back page option--------------
        moveToBackPage();

//        ----------------getText Of pip and upload on the Database-----------
        pipstoreData = takingPipFromUser.toString();
        getTextAndUploadToDatavbse();


    }


    void moveToBackPage() {
        closebtn.setOnClickListener(view -> {
            Intent moveback = new Intent(twittingpageUser.this, twitpage.class);
            startActivity(moveback);
        });
    }

    void getTextAndUploadToDatavbse() {
        submmitPip.setOnClickListener(view -> {
            storepip pipmodel = new storepip(pipstoreData);
            FirebaseDatabase.getInstance().getReference("twitter-clone-332f5-default-rtdb/pipmodel").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .setValue(pipmodel).addOnCompleteListener(task -> {
//                        if (task.isSuccessful()){
//                            Toast.makeText(this, "Successfully", Toast.LENGTH_SHORT).show();
//                        }
//                        else{
//                            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
//                        }
            });
            Toast.makeText(this, "Error 2", Toast.LENGTH_SHORT).show();
        });
    }

}