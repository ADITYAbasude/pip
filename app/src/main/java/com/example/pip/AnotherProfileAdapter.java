package com.example.pip;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AnotherProfileAdapter extends RecyclerView.Adapter<AnotherProfileAdapter.MyAdapter> {
    ArrayList<User> store_pip_data;
    Context context;
    boolean like_dislike = false;
    public final DatabaseReference userPipDataRef = FirebaseDatabase.getInstance().getReference("user").child("UserPost").child("UserPipData");


    public final DatabaseReference userDataRef = FirebaseDatabase.getInstance().getReference("user").child("UserInfo");
    private String uid;
    MediaPlayer mp;

    public AnotherProfileAdapter(Context context, ArrayList<User> store_user_pip, String uid) {
        this.context = context;
        this.store_pip_data = store_user_pip;
        this.uid = uid;
    }


    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.visit_another_profile, parent, false);
        return new MyAdapter(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AnotherProfileAdapter.MyAdapter holder, int position) {
        User user = store_pip_data.get(position);
        holder.userPipName.setText(user.pipuserName);
        holder.pipDateShow.setText(user.pipPostData);
        holder.DateAndTime.setText(user.date);

        userPipDataRef.child(uid).child(user.pip_id).child("ImageUriFromDatabase").
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            holder.setPipImageData.setVisibility(View.VISIBLE);
                            pipDataImageModel ImageModel = snapshot.getValue(pipDataImageModel.class);
                            Glide.with(context).load(Uri.parse(ImageModel.pipImageData)).into(holder.setPipImageData);
                        } else{
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
                    User user1 = snapshot.getValue(User.class);
                    Glide.with(context).load(Uri.parse(user1.User_Profile_Image_Uri)).into(holder.userProfileImg);
                } else {
                    holder.userProfileImg.setImageResource(R.drawable.ic_baseline_account_circle_24);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        pip_like_data_store(user, holder.heart);
        likeStatus(user, holder.heart, holder.heartCount);
        commentCount(holder.CommentCount, user);

        holder.comment.setOnClickListener(view -> {
            Intent moveCommentPage = new Intent(context, userComment.class);
            moveCommentPage.putExtra("userName", holder.userPipName.getText().toString());
            moveCommentPage.putExtra("pipData", holder.pipDateShow.getText().toString());
            moveCommentPage.putExtra("pip_id", user.pip_id);

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


    private  void commentCount(TextView comment_count , User user){
        userPipDataRef.child(uid).child(user.pip_id).child("Comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comment_count.setText(String.valueOf((int) snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void pip_like_data_store(User user, ImageView hert) {
        hert.setOnClickListener(v -> {
            like_dislike = true;
            userPipDataRef.child(uid).child(user.pip_id).child("Likes").addValueEventListener(new ValueEventListener() {
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

    public void likeStatus(User user, ImageView heart, TextView heartCount) {

        userPipDataRef.child(uid).child(user.pip_id).child("Likes").addValueEventListener(new ValueEventListener() {
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
