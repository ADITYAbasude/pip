package com.example.pip.user.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.pip.user.profile.Adapter.MyPostDataControlsAdapter;
import com.example.pip.Models.UserModel;
import com.example.pip.R;
import com.example.pip.databinding.ActivityMyProfileVisitBinding;
import com.example.pip.screens.FollowingFollowerTabScreen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MyProfileVisitScreen extends AppCompatActivity {
    private final DatabaseReference userDataRef = FirebaseDatabase.getInstance().getReference("user").child("UserInfo")
            .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
    private final DatabaseReference userPipDataRef = FirebaseDatabase.getInstance().getReference("user").child("UserPost")
            .child("UserPipData").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    private final ArrayList<UserModel> Store_pip_data = new ArrayList<>();
    private MyPostDataControlsAdapter current_user_pip_data_in_profile;
    private boolean notifyData = true;
    private ActivityMyProfileVisitBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyProfileVisitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.progressBar3.setVisibility(View.VISIBLE);

        Objects.requireNonNull(getSupportActionBar()).hide();

        int modeFlag = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (modeFlag) {
            case Configuration.UI_MODE_NIGHT_YES:
                setUiAsNightMode();
                break;
            case Configuration.UI_MODE_NIGHT_NO:
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                setUiAsLightMode();
                break;
        }

        puUserData();
        setUser_data_in_recyclerView();
//        -----------------Following And Follower Count Program----------------------
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            userDataRef.child("Following").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        binding.FollowingCount.setText(String.valueOf(snapshot.getChildrenCount()));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

            userDataRef.child("Follower").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        binding.FollowerCount.setText(String.valueOf(snapshot.getChildrenCount()));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }

//        -------------------------------------------------------------

        binding.Following.setOnClickListener(v -> FollowingClick());
        binding.Follower.setOnClickListener(v -> FollowingClick());

        binding.progressBar3.setVisibility(View.GONE);

        // TODO: check internet Connection then download the data


        binding.closeProfileScreen.setOnClickListener(view -> onBackPressed());
    }

    private void FollowingClick() {
        Intent intent = new Intent(this, FollowingFollowerTabScreen.class);
        intent.putExtra("usName", binding.putUserName.getText().toString());
        startActivity(intent);
    }

    private void puUserData() {
        userDataRef.child("EditedData").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    UserModel userModel = snapshot.getValue(UserModel.class);
                    assert userModel != null;
                    binding.putUserBio.setText(userModel.Bio);
                    binding.putUserDOB.setText(userModel.dateOfBirth);
                    binding.putUserWebsite.setText(userModel.Website);
                    binding.putUserLocation.setText(userModel.Location);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        userDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userModel = snapshot.getValue(UserModel.class);
                assert userModel != null;
                binding.putUserName.setText(userModel.usName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        userDataRef.child("Profile_Image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    UserModel userModel = snapshot.getValue(UserModel.class);
                    assert userModel != null;
                    Glide.with(MyProfileVisitScreen.this).asBitmap().load(Uri.parse(userModel.User_Profile_Image_Uri))
                            .apply(RequestOptions.placeholderOf(R.drawable.usermodel))
                            .listener(new RequestListener<Bitmap>() {
                                @Override
                                public boolean onLoadFailed(
                                        @Nullable GlideException e,
                                        Object model,
                                        Target<Bitmap> target,
                                        boolean isFirstResource
                                ) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(
                                        Bitmap resource,
                                        Object model,
                                        Target<Bitmap> target,
                                        DataSource dataSource,
                                        boolean isFirstResource
                                ) {
                                    Palette.from(resource).generate(p -> {
                                        assert p != null;
                                        binding.setDynamicColorOfProfile.setCardBackgroundColor(p.getVibrantColor(292929));
                                    });
                                    return false;
                                }
                            })
                            .into(binding.setUserImage);
                } else {
                    binding.setUserImage.setImageResource(R.drawable.usermodel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void setUser_data_in_recyclerView() {
        userPipDataRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Store_pip_data.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        UserModel userModel = ds.getValue(UserModel.class);
                        Store_pip_data.add(userModel);

                    }
                    if (notifyData) {
                        current_user_pip_data_in_profile.notifyDataSetChanged();
                        notifyData = false;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.setUserPipData.setHasFixedSize(true);
        current_user_pip_data_in_profile = new MyPostDataControlsAdapter(this, Store_pip_data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        binding.setUserPipData.setLayoutManager(layoutManager);
        binding.setUserPipData.setAdapter(current_user_pip_data_in_profile);


    }


    void setUiAsNightMode() {
        final Drawable profileUi = ContextCompat.getDrawable(this, R.drawable.pip_show_ui_for_profile);
        final Drawable close = ContextCompat.getDrawable(this, R.drawable.round_close);

        assert profileUi != null;
        assert close != null;

        profileUi.setColorFilter(ContextCompat.getColor(this, R.color.dimNight), PorterDuff.Mode.SRC_ATOP);
        close.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);

        binding.constraintLayout.setBackground(profileUi);
        binding.closeProfileScreen.setImageDrawable(close);

        binding.putUserName.setTextColor(Color.WHITE);
        binding.putUserBio.setTextColor(Color.WHITE);
        binding.putUserDOB.setTextColor(Color.WHITE);
        binding.putUserWebsite.setTextColor(Color.WHITE);
        binding.followerHardText.setTextColor(Color.WHITE);
        binding.followingHardText.setTextColor(Color.WHITE);
        binding.FollowerCount.setTextColor(Color.WHITE);
        binding.FollowingCount.setTextColor(Color.WHITE);


        binding.setUserPipData.setBackgroundResource(R.color.dimNight);

    }

    void setUiAsLightMode() {
        final Drawable profileUi = ContextCompat.getDrawable(this, R.drawable.pip_show_ui_for_profile);
        final Drawable listUi = ContextCompat.getDrawable(this, R.drawable.pip_show_ui_for_profile);
        assert profileUi != null;
        assert listUi != null;
//        profileUi.setColorFilter(ContextCompat.getColor(this, R.color.dimNight), PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }
}