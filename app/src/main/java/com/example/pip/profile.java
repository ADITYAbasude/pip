package com.example.pip;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class profile extends Fragment {


    private TextView UserName, logout, editProfile, profileTextBtn, settingAndPrivacy;
    private DatabaseReference ref;
    private ImageView profileimg;
    private final DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference("user")
            .child("UserInfo").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Profile_Image");
    private final DatabaseReference followingChild_ref = FirebaseDatabase.getInstance().getReference("user").child("UserInfo")
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    private static Uri take_uri;
    private ProgressBar ProfileStatus;
    FirebaseUser uAuth = FirebaseAuth.getInstance().getCurrentUser();

    public profile() {
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
        editProfile = view.findViewById(R.id.editProfile);
        ProfileStatus = view.findViewById(R.id.progressBarstatus);
        profileTextBtn = view.findViewById(R.id.profileTextBtn);
        settingAndPrivacy = view.findViewById(R.id.settingsAndPrivercy);

//        name taking from database
        if (uAuth != null) {

            ref = FirebaseDatabase.getInstance().getReference();
            ref.child("user").child("UserInfo").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
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


//----------------------logout function-------------------
            logoutfuc();


//        ----------------editProfile---------------------
            editProfileClick();


            pickImage_uri_fromFirebase();
            setImage();


//        ---------Following count-------------

//            count_following();

//        ------------setting page -----------------
            invokeSettingAndPrivacy();

//        ----------profile_vist-----
            profileTextBtn.setOnClickListener(v -> {
                Intent move_to_profile_page = new Intent(getContext(), User_profile_visit.class);
                startActivity(move_to_profile_page);
            });
        } else {
            Intent intent = new Intent(getContext(), account.class);
            Toast.makeText(getContext(), "Successfully", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            getActivity().finish();

        }
    }


    //-------------------------logout option function for user -----------------
    void logoutfuc() {
        logout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent inte = new Intent(getContext(), loginActivity.class);
            startActivity(inte);
            getActivity().finish();

        });
    }


    private void setImage() {
        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Glide.with(getContext()).load(take_uri).into(profileimg);
                    ProfileStatus.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void pickImage_uri_fromFirebase() {

        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    take_uri = Uri.parse(user.User_Profile_Image_Uri);
                    ProfileStatus.setVisibility(View.VISIBLE);
                } else {
                    profileimg.setImageResource(R.drawable.ic_baseline_account_circle_24);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void editProfileClick() {
        editProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EditUserProfileActivity.class);
            startActivity(intent);
        });
    }

    private void invokeSettingAndPrivacy() {
        settingAndPrivacy.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), settingsAnsPrivercy.class);
            startActivity(intent);
        });
    }

//    private void count_following() {
//        followingChild_ref.child("Following").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    followingCount.setText(String.valueOf(snapshot.getChildrenCount()));
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        followingChild_ref.child("Follower").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    Follower_count.setText(String.valueOf(snapshot.getChildrenCount()));
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

//    }

}