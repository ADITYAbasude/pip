package com.example.pip;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;


public class twittingpageUser extends AppCompatActivity {
    private EditText takingPipFromUser;
    private Button submmitPip;
    private String pipStoreData, storeUserName, StoreDateAndTime;
    private ImageView selectImage, setSelectImage;
    private Uri selectImageUri, ImageUriFromDatabase;
    private final StorageReference storageRef = FirebaseStorage.getInstance().getReference("pipImage/" + UUID.randomUUID());

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twittingpage_user);


//        ---------id declaration-------------
        takingPipFromUser = findViewById(R.id.takingPipFromUser);
        submmitPip = findViewById(R.id.pipPost);
        selectImage = findViewById(R.id.selectImage);
        setSelectImage = findViewById(R.id.setSelectImage);

        ActionBar ABsupport = getSupportActionBar();
        ABsupport.hide();


//        ----------------getText Of pip and upload on the Database-----------
        pipStoreData = takingPipFromUser.toString();

        getUserNameFromDatabase();
        getTextAndUploadToDatavbse();

        selectImageFunction();

    }


    //    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getTextAndUploadToDatavbse() {
        submmitPip.setOnClickListener(view -> {

            DatabaseReference puskKey = FirebaseDatabase.getInstance().getReference("user").push();
            String getPipData = takingPipFromUser.getText().toString().trim();
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            StoreDateAndTime = simpleDateFormat.format(calendar.getTime()).toString();
            User user = new User(getPipData, storeUserName, puskKey.getKey(), StoreDateAndTime,null);
            if (selectImageUri != null) {
                storageRef.putFile(selectImageUri).addOnSuccessListener(taskSnapshot -> {
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        ImageUriFromDatabase = uri;
                        pipDataImageModel imageModel = new pipDataImageModel(ImageUriFromDatabase.toString());
                        FirebaseDatabase.getInstance().getReference("user").child("UserPost").child("UserPipData")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child(puskKey.getKey()).child("ImageUriFromDatabase").setValue(imageModel);
//                        Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();
                        selectImageUri = null;
                    });
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                });
            }
            FirebaseDatabase.getInstance().getReference("user").child("UserPost").child("UserPipData")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(puskKey.getKey()).setValue(user).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    takingPipFromUser.setText("");
                    setSelectImage.setImageResource(R.drawable.ic_baseline_image_24);
                    setSelectImage.setVisibility(View.INVISIBLE);

                } else {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
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

    private void selectImageFunction() {
        selectImage.setOnClickListener(v -> {
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
            setSelectImage.setVisibility(View.VISIBLE);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectImageUri);
                setSelectImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

    }
}