package com.example.pip.Home.post;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.pip.Models.PostDataImageModel;
import com.example.pip.Models.UserModel;
import com.example.pip.R;
import com.example.pip.databinding.ActivityPostBinding;
import com.example.pip.screens.HomeScreen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;


public class PostScreen extends AppCompatActivity {
    private String storeUserName;
    private String StoreDateAndTime;
    private Uri selectImageUri, ImageUriFromDatabase;
    private final StorageReference storageRef = FirebaseStorage.getInstance().getReference("pipImage/" + UUID.randomUUID());

    private ActivityPostBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.hide();

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

        getUserNameFromDatabase();
        getTextAndUploadToDatavbse();

        selectImageFunction();

        binding.closePostScreen.setOnClickListener(view-> onBackPressed());
    }

    private void getTextAndUploadToDatavbse() {
        binding.pipPost.setOnClickListener(view -> {

            DatabaseReference puskKey = FirebaseDatabase.getInstance().getReference("user").push();
            String getPipData = binding.takingPipFromUser.getText().toString().trim();
            Calendar calendar = Calendar.getInstance();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            StoreDateAndTime = simpleDateFormat.format(calendar.getTime());
            UserModel userModel = new UserModel(getPipData, storeUserName, puskKey.getKey(), StoreDateAndTime,null);
            if (selectImageUri != null) {
                storageRef.putFile(selectImageUri).addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    ImageUriFromDatabase = uri;
                    PostDataImageModel imageModel = new PostDataImageModel(ImageUriFromDatabase.toString());
                    FirebaseDatabase.getInstance().getReference("user").child("UserPost").child("UserPipData")
                            .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                            .child(Objects.requireNonNull(puskKey.getKey())).child("ImageUriFromDatabase").setValue(imageModel);
                    selectImageUri = null;
                })).addOnFailureListener(e -> Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show());
            }
            FirebaseDatabase.getInstance().getReference("user").child("UserPost").child("UserPipData")
                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                    .child(Objects.requireNonNull(puskKey.getKey())).setValue(userModel).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    binding.takingPipFromUser.setText("");
                    binding.setSelectedImage.setImageResource(R.drawable.ic_baseline_image_24);
                    binding.setSelectedImage.setVisibility(View.INVISIBLE);

                } else {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void getUserNameFromDatabase() {
        FirebaseDatabase.getInstance().getReference("user").child("UserInfo")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        assert userModel != null;
                        storeUserName = userModel.usName;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private void selectImageFunction() {
        binding.selectImage.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "pick Image"), 1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            selectImageUri = data.getData();
            binding.setSelectedImage.setVisibility(View.VISIBLE);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectImageUri);
                binding.setSelectedImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void setUiAsNightMode() {
        final Drawable close = ContextCompat.getDrawable(this, R.drawable.round_close);
        final Drawable pickImage = ContextCompat.getDrawable(this , R.drawable.add_photo);
        assert close != null;
        assert pickImage != null;
        close.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        pickImage.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        binding.closePostScreen.setImageDrawable(close);
        binding.selectImage.setImageDrawable(pickImage);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Login your account");
    }

    void setUiAsLightMode() {
        final Drawable close = ContextCompat.getDrawable(this, R.drawable.round_close);
        final Drawable pickImage = ContextCompat.getDrawable(this , R.drawable.add_photo);
        assert close != null;
        assert pickImage != null;
        close.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_ATOP);
        pickImage.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_ATOP);
        binding.closePostScreen.setImageDrawable(close);
        binding.selectImage.setImageDrawable(pickImage);
        Objects.requireNonNull(getSupportActionBar()).setTitle(Html.fromHtml("<font color='#000000'>Login your account</font>"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(this , HomeScreen.class);
        overridePendingTransition(R.anim.activity_in  , R.anim.activity_out);
    }
}