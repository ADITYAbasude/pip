package com.example.pip;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class profile extends Fragment {


    TextView UserName, logout;
    FirebaseUser user;
    DatabaseReference ref;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("Image");
    private StorageReference refrence = FirebaseStorage.getInstance().getReference();
    private static final int GALLAY_REQUEST_CODE = 123;
    ImageView profileimg;
    String userid;
    Uri ImageData;

    public profile() {
    }

    public static profile newInstance() {
        profile fragment = new profile();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        id declaration
        UserName = view.findViewById(R.id.UserName);
        profileimg = view.findViewById(R.id.profileimg);
        logout = view.findViewById(R.id.logout);


//        name taking from database
        user = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("user");
        userid = user.getUid();
        ref.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userprofile = snapshot.getValue(User.class);
                String usernamesertext = userprofile.usName;
                SharedPreferences sp = getContext().getSharedPreferences("username", MODE_PRIVATE);
                SharedPreferences.Editor editsp = sp.edit();
                editsp.putString("name", usernamesertext);
                editsp.apply();
                UserName.setText(usernamesertext);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        SharedPreferences sp2 = getContext().getSharedPreferences("username", MODE_PRIVATE);
        String takeNamefromSP = sp2.getString("name", " ");
        UserName.setText(takeNamefromSP);

//        --------*******************---------------

//            ------imgae select from user------
        profileimg.setOnClickListener(view1 -> {
            Intent imagesIntent = new Intent();
            imagesIntent.setType("image/*");
            imagesIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(imagesIntent, "pick image"), GALLAY_REQUEST_CODE);
            if (ImageData != null) {
                uploadImage(ImageData);
            }


        });

//----------------------logout function-------------------
        logoutfuc();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == GALLAY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ImageData = data.getData();
            profileimg.setImageURI(ImageData);
        }
    }
//-------------------------logout option function for user -----------------
    void logoutfuc() {
        logout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(getContext(), "you LogOut", Toast.LENGTH_SHORT).show();
            Intent inte = new Intent(getContext(), loginActivity.class);
            startActivity(inte);
        });
    }
//------------------imgae upload on the database--------------
    void uploadImage(Uri imageuri) {
        StorageReference fileref = refrence.child(System.currentTimeMillis() + "." + getFileExtansion(imageuri));
        fileref.putFile(imageuri).addOnSuccessListener(taskSnapshot -> fileref.getDownloadUrl().addOnSuccessListener(uri -> {
            imageModel model = new imageModel(uri.toString());
            String modeli = root.push().getKey();
            root.child(String.valueOf(model)).setValue(modeli);
        })).addOnFailureListener(e -> Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show());
    }

    String getFileExtansion(Uri imuri ){
        ContentResolver cr = getContext().getContentResolver();
        MimeTypeMap mtm = MimeTypeMap.getSingleton();
        return mtm.getExtensionFromMimeType(cr.getType(imuri));

    }
}