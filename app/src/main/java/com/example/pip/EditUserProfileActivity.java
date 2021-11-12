package com.example.pip;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class EditUserProfileActivity extends AppCompatActivity {
    private TextInputEditText datePicker , UserName , UserBio , UserLocation  ,UserWebsite ;
    private Button saveChanges;
    String userInfo = FirebaseAuth.getInstance().getCurrentUser().getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user_profile);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this , R.color.purple_500));
//```````````````````````id declaration```````````````````````````````````````

        UserName = findViewById(R.id.UserName);
        UserBio = findViewById(R.id.UserBio);
        UserLocation = findViewById(R.id.UserLocation);
        UserWebsite = findViewById(R.id.UserWebsite);
        datePicker = findViewById(R.id.datePicker);
        saveChanges = findViewById(R.id.saveChanges);



        datePicker.setOnClickListener(v -> {
            datePickerFunctionInvoked();
        });

        saveChanges.setOnClickListener(v -> {
            setFormatOfWebsite();

        });









        takeUserNameFromDataBase();

    }

    private void setFormatOfWebsite() {

    }




    private void datePickerFunctionInvoked() {
        Calendar calendar  = Calendar.getInstance();
        int Year = calendar.get(Calendar.YEAR);
        int Month = calendar.get(Calendar.MONTH);
        int Day = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            month += 1;
            datePicker.setText(dayOfMonth + "/" + month + "/" + year);
        }, Year ,Month , Day);
        datePickerDialog.show();
    }

    private void takeUserNameFromDataBase(){
        FirebaseDatabase.getInstance().getReference("user").child("UserInfo").child(userInfo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User getUserData = snapshot.getValue(User.class);

                UserName.setText(String.valueOf(getUserData.usName));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }






}