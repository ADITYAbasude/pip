package com.example.pip.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pip.Models.PostDataImageModel;
import com.example.pip.Models.UserModel;
import com.example.pip.R;
import com.example.pip.screens.CommentScreen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AnotherUserPostAdaptor extends RecyclerView.Adapter<AnotherUserPostAdaptor.MyAdapter> {
    ArrayList<UserModel> store_pip_data;
    Context context;
    boolean like_dislike = false;
    public final DatabaseReference userPipDataRef = FirebaseDatabase.getInstance().getReference("user").child("UserPost").child("UserPipData");


    public final DatabaseReference userDataRef = FirebaseDatabase.getInstance().getReference("user").child("UserInfo");
    private String uid;
    MediaPlayer mp;

    public AnotherUserPostAdaptor(Context context, ArrayList<UserModel> store_user_Model_pip, String uid) {
        this.context = context;
        this.store_pip_data = store_user_Model_pip;
        this.uid = uid;
    }


    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.visit_another_profile, parent, false);
        return new MyAdapter(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AnotherUserPostAdaptor.MyAdapter holder, int position) {
        UserModel userModel = store_pip_data.get(position);
        holder.userPipName.setText(userModel.pipuserName);
        holder.pipDateShow.setText(userModel.pipPostData);
        holder.DateAndTime.setText(userModel.date);

        userPipDataRef.child(uid).child(userModel.pip_id).child("ImageUriFromDatabase").
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            holder.setPipImageData.setVisibility(View.VISIBLE);
                            PostDataImageModel ImageModel = snapshot.getValue(PostDataImageModel.class);
                            Glide.with(context).load(Uri.parse(ImageModel.pipImageData)).into(holder.setPipImageData);
                        } else {
                            holder.setPipImageData.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        userDataRef.child(uid).child("Profile_Image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    UserModel userModel1 = snapshot.getValue(UserModel.class);
                    Glide.with(context).load(Uri.parse(userModel1.User_Profile_Image_Uri)).into(holder.userProfileImg);
                } else {
                    holder.userProfileImg.setImageResource(R.drawable.usermodel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        pip_like_data_store(userModel, holder.heart);
        likeStatus(userModel, holder.heart, holder.heartCount);
        commentCount(holder.CommentCount, userModel);

        holder.comment.setOnClickListener(view -> {
            Intent moveCommentPage = new Intent(context, CommentScreen.class);
            moveCommentPage.putExtra("userName", holder.userPipName.getText().toString());
            moveCommentPage.putExtra("pipData", holder.pipDateShow.getText().toString());
            moveCommentPage.putExtra("pip_id", userModel.pip_id);
            context.startActivity(moveCommentPage);

        });

        holder.share.setOnClickListener(view -> {
            Intent share = new Intent();
            share.setAction(Intent.ACTION_SEND);
            share.putExtra(Intent.EXTRA_TEXT, holder.pipDateShow.getText().toString());
            share.setType("text/plain");
            Intent shareIntent = Intent.createChooser(share, "Pip Post");
            context.startActivity(shareIntent);
        });


    }


    private void commentCount(TextView comment_count, UserModel userModel) {
        userPipDataRef.child(uid).child(userModel.pip_id).child("Comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comment_count.setText(String.valueOf((int) snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void pip_like_data_store(UserModel userModel, ImageView hert) {
        hert.setOnClickListener(v -> {
            like_dislike = true;
            userPipDataRef.child(uid).child(userModel.pip_id).child("Likes").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (like_dislike) {
                        if (snapshot.hasChild((FirebaseAuth.getInstance().getCurrentUser().getUid()))) {
                            snapshot.getRef().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(null);
                            hert.setImageResource(R.drawable.heart);
                            like_dislike = false;
                        } else {
                            hert.setImageResource(R.drawable.heartred);
                            mp = MediaPlayer.create(context, R.raw.heart_click_sound);
                            mp.setOnPreparedListener(mp1 -> {
                                mp.start();
                            });
                            snapshot.getRef().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
                            like_dislike = false;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        });

    }

    public void likeStatus(UserModel userModel, ImageView heart, TextView heartCount) {

        userPipDataRef.child(uid).child(userModel.pip_id).child("Likes").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if ((snapshot.hasChild(FirebaseAuth.getInstance().getUid()))) {
                    heart.setImageResource(R.drawable.heartred);
                    heartCount.setText(Integer.toString((int) snapshot.getChildrenCount()));

                } else {
                    heart.setImageResource(R.drawable.heart);
                    heartCount.setText(Integer.toString((int) snapshot.getChildrenCount()));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    @Override
    public int getItemCount() {
        return store_pip_data.size();
    }

    public class MyAdapter extends RecyclerView.ViewHolder {
        TextView userPipName, pipDateShow, heartCount, CommentCount, DateAndTime;
        ImageView heart, comment, share, userProfileImg, setPipImageData;

        public MyAdapter(@NonNull View itemView) {
            super(itemView);
            userPipName = itemView.findViewById(R.id.UserNameInPip);
            pipDateShow = itemView.findViewById(R.id.PipPostDate);
            heart = itemView.findViewById(R.id.heart);
            comment = itemView.findViewById(R.id.comment);
            heartCount = itemView.findViewById(R.id.heartCount);
            CommentCount = itemView.findViewById(R.id.commentCount);
            share = itemView.findViewById(R.id.share);
            userProfileImg = itemView.findViewById(R.id.userProfileImg);
            DateAndTime = itemView.findViewById(R.id.DateAndTime);
            setPipImageData = itemView.findViewById(R.id.setPipImageData);
        }
    }
}
