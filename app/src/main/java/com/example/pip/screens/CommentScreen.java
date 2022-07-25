package com.example.pip.screens;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.pip.Adapters.CommentAdapter;
import com.example.pip.Models.CommentModel;
import com.example.pip.Models.PostDataImageModel;
import com.example.pip.Models.UserModel;
import com.example.pip.R;
import com.example.pip.databinding.UsercommentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class CommentScreen extends AppCompatActivity {

    private final DatabaseReference userDataRef = FirebaseDatabase.getInstance().getReference("user").child("UserInfo");
    private final DatabaseReference userPipDataRef = FirebaseDatabase.getInstance().getReference("user")
            .child("UserPost").child("UserPipData");
    private final ArrayList<CommentModel> storeUserReply = new ArrayList<>();
    private CommentAdapter commentAdapter;
    private String storeUserName;
    private String Store_pip_id;
    private String u_id;
    private String ImageUri;
    private UsercommentBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = UsercommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


//        ---------------------ActionBar hide------------------
        Objects.requireNonNull(getSupportActionBar()).hide();


//        ----------------------- manage mode ---------------
        int modeFlag = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (modeFlag == Configuration.UI_MODE_NIGHT_YES) {
            binding.setUserNameInCommentSection.setTextColor(Color.WHITE);
            binding.setUserPipDataInCommentSection.setTextColor(Color.WHITE);

            final Drawable send = ContextCompat.getDrawable(this, R.drawable.send);
            assert send != null;
            send.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
            binding.sendBtn.setBackground(send);

            final Drawable close = ContextCompat.getDrawable(this, R.drawable.arrow_back);
            assert close != null;
            close.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
            binding.closeCommentScreen.setBackground(close);

            binding.textView4.setTextColor(Color.WHITE);

        }

//-------------------- getExtras from intent -----------------
        Bundle getData = getIntent().getExtras();

        binding.setUserNameInCommentSection.setText(getData.getString("userName"));
        binding.setUserPipDataInCommentSection.setText(getData.getString("pipData"));
        Store_pip_id = getData.getString("pip_id");

// ------------------ post image ------------------
        getPostImageDataFromFireBase();

        userDataRef.orderByChild("usName").equalTo(binding.setUserNameInCommentSection.getText().toString()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                u_id = snapshot.getRef().getKey();
                setUserImage(u_id);
                setUserCommentToRecycler(u_id);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userDataRef.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .child("Profile_Image").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            UserModel userModel = snapshot.getValue(UserModel.class);
                            assert userModel != null;
                            ImageUri = userModel.User_Profile_Image_Uri;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        ImageUri = null;
                    }
                });

        userDataRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
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

        binding.showComment.setHasFixedSize(true);
        commentAdapter = new CommentAdapter(this, storeUserReply);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        binding.showComment.setLayoutManager(layoutManager);
        binding.showComment.setAdapter(commentAdapter);

        binding.progressBarInRecyclerView.setVisibility(View.VISIBLE);
        takeCommentFromUser();


        binding.closeCommentScreen.setOnClickListener(v -> onBackPressed());

    }

    private void setUserImage(String uid) {
        userDataRef.child(uid).child("Profile_Image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    UserModel userModel = snapshot.getValue(UserModel.class);
                    assert userModel != null;
                    Glide.with(CommentScreen.this).load(Uri.parse(userModel.User_Profile_Image_Uri)).into(binding.userPhotoInCommentPage);
                } else {
                    binding.userPhotoInCommentPage.setImageResource(R.drawable.usermodel);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void takeCommentFromUser() {
        binding.sendBtn.setOnClickListener(v -> {
            if (binding.writeComment.getText() != null && binding.writeComment.getText().length() != 0) {
                CommentModel comment = new CommentModel(FirebaseAuth.getInstance().getUid(), binding.writeComment.getText().toString(), ImageUri
                        , storeUserName);
                userPipDataRef.child(u_id).child(Store_pip_id).child("Comments").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        snapshot.getRef().push().setValue(comment);
                        binding.writeComment.setText("");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }


    private void setUserCommentToRecycler(String uid) {

        binding.progressBarInRecyclerView.setVisibility(View.GONE);

        userPipDataRef.child(uid).child(Store_pip_id).child("Comments").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    storeUserReply.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        CommentModel comment = ds.getValue(CommentModel.class);
                        storeUserReply.add(comment);
                    }
                    commentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getPostImageDataFromFireBase() {
        userPipDataRef.child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child(Store_pip_id).
                child("ImageUriFromDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            PostDataImageModel imageModel = snapshot.getValue(PostDataImageModel.class);
                            assert imageModel != null;
                            String getPostImage = imageModel.pipImageData;
                            if (getPostImage != null) {
                                binding.setPostImage.setVisibility(View.VISIBLE);
                                Glide.with(CommentScreen.this).load(Uri.parse(getPostImage))
                                        .apply(RequestOptions.placeholderOf(R.drawable.ic_baseline_image_24))
                                        .listener(new RequestListener<Drawable>() {
                                            @Override
                                            public boolean onLoadFailed(
                                                    @Nullable GlideException e,
                                                    Object model,
                                                    Target<Drawable> target,
                                                    boolean isFirstResource
                                            ) {
                                                binding.setPostImage.setBackgroundResource(R.drawable.ic_baseline_image_24);
                                                Toast.makeText(CommentScreen.this, "Image failed to download", Toast.LENGTH_SHORT).show();
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(
                                                    Drawable resource,
                                                    Object model,
                                                    Target<Drawable> target,
                                                    DataSource dataSource,
                                                    boolean isFirstResource
                                            ) {

                                                return false;
                                            }
                                        })
                                        .into(binding.setPostImage);
                            } else {
                                binding.setPostImage.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
