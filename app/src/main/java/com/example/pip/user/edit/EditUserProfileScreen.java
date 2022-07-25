package com.example.pip.user.edit;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.pip.Models.UserModel;
import com.example.pip.R;
import com.example.pip.databinding.ActivityEditUserProfileBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

public class EditUserProfileScreen extends AppCompatActivity {
    private String bio, name, location, website, dateOfBirth;
    private static Uri ImageData, take_uri;
    private final static String userInfo = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

    private ActivityEditUserProfileBinding binding;

    private final DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference("user")
            .child("UserInfo").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("Profile_Image");
    private final StorageReference storageRef = FirebaseStorage.getInstance().getReference("Profile_Photo/" + UUID.randomUUID());

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).hide();


        View v2 = binding.conID;


        int modeFlag = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (modeFlag) {
            case Configuration.UI_MODE_NIGHT_YES:
                setUiAsNightMode();
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                setUiAsLightMode();
                break;
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                setUiAsLightMode();
                break;
        }

        binding.datePicker.setOnTouchListener((v, event) -> {
            EditUserProfileScreen.this.datePickerFunctionInvoked();
            return false;
        });


        binding.removeImg.setOnClickListener(v -> {
            Snackbar.make(v2, "It will take some time to update", Snackbar.LENGTH_LONG).show();
            firebaseRef.setValue(null);
        });

        binding.saveChanges.setOnClickListener(v -> {
            binding.editTextProgressBar.setVisibility(View.VISIBLE);
            name = Objects.requireNonNull(binding.UserName.getText()).toString();
            bio = Objects.requireNonNull(binding.UserBio.getText()).toString();
            location = Objects.requireNonNull(binding.UserLocation.getText()).toString();
            dateOfBirth = Objects.requireNonNull(binding.datePicker.getText()).toString();
            website = Objects.requireNonNull(binding.UserWebsite.getText()).toString();


            if (!dateOfBirth.matches("^[0-3]?[0-9]/[0-3]?[0-9]/(?:[0-9]{2})?[0-9]{2}$") && !dateOfBirth.isEmpty()) {
                binding.datePicker.setError("Enter a proper date format");
                binding.datePicker.requestFocus();
                binding.editTextProgressBar.setVisibility(View.INVISIBLE);
            } else if (!website.matches("((http|https)://)(www.)?[a-zA-Z0-9@:%._\\+~#?&//=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%._\\+~#?&//=]*)") && !website.isEmpty()) {
                binding.UserWebsite.setError("Follow this pattern ((http or https)://xyz.xyz)");
                binding.UserWebsite.requestFocus();
                binding.editTextProgressBar.setVisibility(View.INVISIBLE);
            } else {
                FirebaseDatabase.getInstance().getReference("user").child("UserInfo").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                snapshot.getRef().child("usName").setValue(name);
                                Snackbar.make(v2, "It will take some time to update", Snackbar.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

                UserModel userModel = new UserModel(bio, location, website, dateOfBirth);
                FirebaseDatabase.getInstance().getReference("user").child("UserInfo").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("EditedData").setValue(userModel);

                if (ImageData != null) {
                    uploadImage(ImageData);
                }
                binding.editTextProgressBar.setVisibility(View.GONE);

            }
        });

        binding.setImageOfUser.setOnClickListener(v -> {
            Intent image = new Intent();
            image.setType("image/*");
            image.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(image, "pick image"), 1);

        });

        puckImage_uri_fromFirebase();
        takeUserDataFromDataBase();
        setImage();


        binding.closeEditScreen.setOnClickListener(view -> onBackPressed());

    }


    private void setImage() {
        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Glide.with(EditUserProfileScreen.this).load(take_uri).into(binding.setImageOfUser);
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
                    UserModel userModel = snapshot.getValue(UserModel.class);
                    assert userModel != null;
                    take_uri = Uri.parse(userModel.User_Profile_Image_Uri);
                } else {
                    binding.setImageOfUser.setImageResource(R.drawable.usermodel);
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
                binding.setImageOfUser.setImageBitmap(imageBitMap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(Uri imageData) {
        if (imageData.toString().length() != 0) {
            storageRef.putFile(imageData).addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                UserModel userModel = new UserModel(uri.toString());
                firebaseRef.setValue(userModel);
            })).addOnFailureListener(e -> Toast.makeText(EditUserProfileScreen.this, "Image fail to uploaded", Toast.LENGTH_SHORT).show());
        }
    }


    private void datePickerFunctionInvoked() {
        Calendar calendar = Calendar.getInstance();
        int Year = calendar.get(Calendar.YEAR);
        int Month = calendar.get(Calendar.MONTH);
        int Day = calendar.get(Calendar.DATE);

        @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            month += 1;
            binding.datePicker.setText(dayOfMonth + "/" + month + "/" + year);
        }, Year, Month, Day);
        datePickerDialog.show();
    }

    private void takeUserDataFromDataBase() {
        FirebaseDatabase.getInstance().getReference("user").child("UserInfo").child(userInfo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel getUserModelData = snapshot.getValue(UserModel.class);
                assert getUserModelData != null;
                binding.UserName.setText(String.valueOf(getUserModelData.usName));
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
                    UserModel userModel = snapshot.getValue(UserModel.class);
                    assert userModel != null;
                    binding.UserBio.setText(userModel.Bio);
                    binding.UserLocation.setText(String.valueOf(userModel.Location));
                    binding.UserWebsite.setText(String.valueOf(userModel.Website));
                    binding.datePicker.setText(String.valueOf(userModel.dateOfBirth));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    void setUiAsNightMode() {
        final Drawable close = ContextCompat.getDrawable(this, R.drawable.round_close);
        assert close != null;
        close.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        binding.closeEditScreen.setImageDrawable(close);
    }

    void setUiAsLightMode() {
        final Drawable close = ContextCompat.getDrawable(this, R.drawable.round_close);
        assert close != null;
        close.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_ATOP);
        binding.closeEditScreen.setImageDrawable(close);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }
}