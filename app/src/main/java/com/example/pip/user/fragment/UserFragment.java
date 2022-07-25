package com.example.pip.user.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.pip.Models.UserModel;
import com.example.pip.R;
import com.example.pip.auth.screens.LoginUserScreen;
import com.example.pip.databinding.FragmentProfileBinding;
import com.example.pip.screens.StartScreen;
import com.example.pip.user.edit.EditUserProfileScreen;
import com.example.pip.user.profile.MyProfileVisitScreen;
import com.example.pip.user.settings.SettingsScreen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class UserFragment extends Fragment {


    private final static DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference("user")
            .child("UserInfo").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("Profile_Image");
    private static Uri take_uri;
    private final FirebaseUser uAuth = FirebaseAuth.getInstance().getCurrentUser();

    public UserFragment() {
    }

    private FragmentProfileBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

//        name taking from database
        if (uAuth != null) {

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.child("user").child("UserInfo").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            UserModel userprofile = snapshot.getValue(UserModel.class);
                            assert userprofile != null;
                            String usernamesertext = userprofile.usName;
                            SharedPreferences sp = requireContext().getSharedPreferences("username", MODE_PRIVATE);
                            SharedPreferences.Editor editsp = sp.edit();
                            editsp.putString("name", usernamesertext);
                            editsp.apply();
                            binding.UserName.setText(usernamesertext);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            SharedPreferences sp2 = requireContext().getSharedPreferences("username", MODE_PRIVATE);
            String takeNamefromSP = sp2.getString("name", " ");
            binding.UserName.setText(takeNamefromSP);


//----------------------logout function-------------------
            logOutFuc();


//        ----------------editProfile---------------------
            editProfileClick();


            pickImage_uri_fromFirebase();
            setImage();


//        ---------Following count-------------

//            count_following();

//        ------------setting page -----------------
            invokeSettingAndPrivacy();

//        ----------profile_vist-----
            binding.profileTextBtn.setOnClickListener(v -> {
                Intent move_to_profile_page = new Intent(getContext(), MyProfileVisitScreen.class);
                startActivity(move_to_profile_page);
            });
        } else {
            Intent intent = new Intent(getContext(), StartScreen.class);
            Toast.makeText(getContext(), "Successfully", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            requireActivity().finish();
        }
    }

    //-------------------------logout option function for user -----------------
    void logOutFuc() {
        binding.logout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent inte = new Intent(getContext(), LoginUserScreen.class);
            startActivity(inte);
            requireActivity().finish();

        });
    }


    private void setImage() {
        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Glide.with(requireContext()).asBitmap().load(take_uri)
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
                                        binding.imageStatusColor.setCardBackgroundColor(p.getLightVibrantColor(292929));
                                    });

                                    return false;
                                }
                            }).into(binding.profileImg);
                    binding.progressBarstatus.setVisibility(View.GONE);
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
                    UserModel userModel = snapshot.getValue(UserModel.class);
                    assert userModel != null;
                    take_uri = Uri.parse(userModel.User_Profile_Image_Uri);
                    binding.progressBarstatus.setVisibility(View.VISIBLE);
                } else {
                    binding.profileImg.setImageResource(R.drawable.usermodel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void editProfileClick() {
        binding.editProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EditUserProfileScreen.class);
            startActivity(intent);
        });
    }

    private void invokeSettingAndPrivacy() {
        binding.settingsAndPrivercy.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SettingsScreen.class);
            startActivity(intent);
        });
    }


    void setUiAsNightMode() {
        final Drawable UI = ContextCompat.getDrawable(requireContext(), R.drawable.profile_ui);
        assert UI != null;
        UI.setColorFilter(ContextCompat.getColor(requireContext(), R.color.dimNight), PorterDuff.Mode.SRC_ATOP);
        binding.functionSets.setBackground(UI);

        final Drawable manageAccount = ContextCompat.getDrawable(requireContext(), R.drawable.manage_accounts);
        assert manageAccount != null;
        manageAccount.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white), PorterDuff.Mode.SRC_ATOP);
        binding.manageAccountImage.setImageDrawable(manageAccount);

        final Drawable setting = ContextCompat.getDrawable(requireContext(), R.drawable.settings);
        assert setting != null;
        setting.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white), PorterDuff.Mode.SRC_ATOP);
        binding.settingImage.setImageDrawable(setting);

        final Drawable exit = ContextCompat.getDrawable(requireContext(), R.drawable.exit);
        assert exit != null;
        exit.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white), PorterDuff.Mode.SRC_ATOP);
        binding.exitImage.setImageDrawable(exit);

        binding.userIconImage.setImageResource(R.drawable.white_user);

        binding.profileTextBtn.setTextColor(Color.WHITE);
        binding.editProfile.setTextColor(Color.WHITE);
        binding.settingsAndPrivercy.setTextColor(Color.WHITE);
        binding.UserName.setTextColor(Color.WHITE);
    }

    void setUiAsLightMode() {
    }

}