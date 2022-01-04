package com.example.pip;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

public class EditUserProfileActivity extends AppCompatActivity {
    private TextInputEditText datePicker, UserName, UserBio, UserLocation, UserWebsite;
    private Button saveChanges;
    private String bio, name, location, website, dateOfBirth;
    private ImageView setImageOfUser;
    private static Uri ImageData, take_uri;
    private ProgressBar editText_progressBar;
    private TextView removeImage;
    String userInfo = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private final DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference("user")
            .child("UserInfo").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Profile_Image");
    private final StorageReference storageRef = FirebaseStorage.getInstance().getReference("Profile_Photo/" + UUID.randomUUID());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user_profile);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.purple_500));
//```````````````````````id declaration```````````````````````````````````````

        UserName = findViewById(R.id.UserName);
        UserBio = findViewById(R.id.UserBio);
        UserLocation = findViewById(R.id.UserLocation);
        UserWebsite = findViewById(R.id.UserWebsite);
        datePicker = findViewById(R.id.datePicker);
        saveChanges = findViewById(R.id.saveChanges);
        setImageOfUser = findViewById(R.id.setImageOfUser);
        editText_progressBar = findViewById(R.id.editText_progressBar);
        View v2 = findViewById(R.id.conID);
        removeImage = findViewById(R.id.remove_img);

        datePicker.setOnClickListener(v -> datePickerFunctionInvoked());

        removeImage.setOnClickListener(v -> {
            firebaseRef.setValue(null);
        });


        saveChanges.setOnClickListener(v -> {
            editText_progressBar.setVisibility(View.VISIBLE);
            name = UserName.getText().toString();
            bio = UserBio.getText().toString();
            location = UserLocation.getText().toString();
            dateOfBirth = datePicker.getText().toString();
            website = UserWebsite.getText().toString();
            FirebaseDatabase.getInstance().getReference("user").child("UserInfo").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            snapshot.getRef().child("usName").setValue(name);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });


            User user = new User(bio, location, website, dateOfBirth);
            FirebaseDatabase.getInstance().getReference("user").child("UserInfo").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("EditedData").setValue(user);

            if (ImageData != null) {
                uploadImage(ImageData);
            }
            editText_progressBar.setVisibility(View.GONE);
            Snackbar.make(v2 , "Successful" , Snackbar.LENGTH_LONG).show();

        });


        setImageOfUser.setOnClickListener(v -> {
            Intent image = new Intent();
            image.setType("image/*");
            image.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(image, "pick image"), 1);

        });


        puckImage_uri_fromFirebase();
        takeUserDataFromDataBase();
        setImage();

    }


    private void setImage() {
        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Glide.with(EditUserProfileActivity.this).load(take_uri).into(setImageOfUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void puckImage_uri_fromFirebase() {

        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    take_uri = Uri.parse(user.User_Profile_Image_Uri);
                } else {
                    setImageOfUser.setImageResource(R.drawable.ic_baseline_account_circle_24);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            ImageData = data.getData();
            try {
                Bitmap imageBitMap = MediaStore.Images.Media.getBitmap(getContentResolver(), ImageData);
                setImageOfUser.setImageBitmap(imageBitMap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(Uri imageData) {
        if (imageData.toString().length() != 0) {
            storageRef.putFile(imageData).addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                User user = new User(uri.toString());
                firebaseRef.setValue(user);
            })).addOnFailureListener(e -> Toast.makeText(EditUserProfileActivity.this, "Image fail to uploaded", Toast.LENGTH_SHORT).show());
        }
    }


    private void datePickerFunctionInvoked() {
        Calendar calendar = Calendar.getInstance();
        int Year = calendar.get(Calendar.YEAR);
        int Month = calendar.get(Calendar.MONTH);
        int Day = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            month += 1;
            datePicker.setText(dayOfMonth + "/" + month + "/" + year);
        }, Year, Month, Day);
        datePickerDialog.show();
    }

    private void takeUserDataFromDataBase() {
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

        FirebaseDatabase.getInstance().getReference("user").child("UserInfo").child(userInfo).child("EditedData").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // if user data in firebase  , then it will show the data ,  otherwise it will show the null values in edittext . So you can write and create your personal information
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    UserBio.setText(user.Bio);
                    UserLocation.setText(String.valueOf(user.Location));
                    UserWebsite.setText(String.valueOf(user.Website));
                    datePicker.setText(String.valueOf(user.dateOfBirth));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}