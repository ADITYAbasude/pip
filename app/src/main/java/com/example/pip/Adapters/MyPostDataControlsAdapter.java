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
import com.example.pip.R;
import com.example.pip.Models.UserModel;
import com.example.pip.Models.PostDataImageModel;
import com.example.pip.screens.CommentScreen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MyPostDataControlsAdapter extends RecyclerView.Adapter<MyPostDataControlsAdapter.MyAdapter> {
    ArrayList<UserModel> store_pip_data;
    Context context;
    boolean like_dislike = false;
    public final DatabaseReference userPipDataRef = FirebaseDatabase.getInstance().getReference("user").child("UserPost").child("UserPipData")
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    MediaPlayer mp;


    public final DatabaseReference userDataRef = FirebaseDatabase.getInstance().getReference("user").child("UserInfo").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    String u_id = FirebaseAuth.getInstance().getUid();

    public MyPostDataControlsAdapter(Context context, ArrayList<UserModel> store_user_Model_pip) {
        this.context = context;
        this.store_pip_data = store_user_Model_pip;
    }


    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.profile_user_pip_data_structure, parent, false);
        return new MyAdapter(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter holder, int position) {

        UserModel userModel = store_pip_data.get(position);
        holder.userPipName2.setText(userModel.pipuserName);
        holder.pipDateShow2.setText(userModel.pipPostData);
        holder.pipDateAndTime.setText(userModel.date);


        likeStatus(userModel, holder.heart2, holder.heartCount2);


        userPipDataRef.child(userModel.pip_id).child("ImageUriFromDatabase").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    holder.CurrentUserPipDataImage.setVisibility(View.VISIBLE);
                    PostDataImageModel ImageModel = snapshot.getValue(PostDataImageModel.class);
                    Glide.with(context).load(Uri.parse(ImageModel.pipImageData)).into(holder.CurrentUserPipDataImage);
                } else {
                    holder.CurrentUserPipDataImage.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        userDataRef.child("Profile_Image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    UserModel userModel1 = snapshot.getValue(UserModel.class);
                    Glide.with(context).load(Uri.parse(userModel1.User_Profile_Image_Uri)).into(holder.User_image);
                } else {
                    holder.User_image.setImageResource(R.drawable.usermodel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        delect_pip_Data(holder.delect, userModel);
        pip_like_data_store(userModel, holder.heart2);
        commentCount(holder.CommentCount2, userModel);

        holder.comment2.setOnClickListener(view -> {
            Intent moveCommentPage = new Intent(context, CommentScreen.class);
            moveCommentPage.putExtra("userName", holder.userPipName2.getText().toString());
            moveCommentPage.putExtra("pipData", holder.pipDateShow2.getText().toString());
            moveCommentPage.putExtra("pip_id", userModel.pip_id);
            context.startActivity(moveCommentPage);

        });

        holder.share2.setOnClickListener(view -> {
            Intent share = new Intent();
            share.setAction(Intent.ACTION_SEND);
            share.putExtra(Intent.EXTRA_TEXT, holder.pipDateShow2.getText().toString());
            share.setType("text/plain");
            Intent shareIntent = Intent.createChooser(share, "Pip Post");
            context.startActivity(shareIntent);
        });


    }


    private void commentCount(TextView comment_count, UserModel userModel) {
        userPipDataRef.child(userModel.pip_id).child("Comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comment_count.setText(String.valueOf((int) snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void delect_pip_Data(ImageView delect, UserModel userModel) {
        delect.setOnClickListener(v ->
                userPipDataRef.child(userModel.pip_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        snapshot.getRef().setValue(null);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }));


    }

    private void pip_like_data_store(UserModel userModel, ImageView hert) {
        hert.setOnClickListener(v -> {
            like_dislike = true;
            userPipDataRef.child(userModel.pip_id).child("Likes").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (like_dislike) {
                        if (snapshot.hasChild(u_id)) {
                            snapshot.getRef().child(u_id).setValue(null);
                            hert.setImageResource(R.drawable.heart);
                            like_dislike = false;
                        } else {
                            hert.setImageResource(R.drawable.heartred);
                            mp = MediaPlayer.create(context, R.raw.heart_click_sound);
                            mp.setOnPreparedListener(mp -> {
                                mp.start();
                            });
                            snapshot.getRef().child(u_id).setValue(true);
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

        userPipDataRef.child(userModel.pip_id).child("Likes").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.hasChild(u_id)) {
                    heart.setImageResource(R.drawable.heartred);
                    heartCount.setText(Integer.toString((int) snapshot.getChildrenCount()));
                    ;
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

        protected TextView userPipName2, pipDateShow2, heartCount2, CommentCount2, pipDateAndTime;
        protected ImageView heart2, comment2, share2, User_image, delect, CurrentUserPipDataImage;


        public MyAdapter(@NonNull View itemView) {
            super(itemView);
            userPipName2 = itemView.findViewById(R.id.userNameInPip2);
            pipDateShow2 = itemView.findViewById(R.id.pipPostDate2);
            heart2 = itemView.findViewById(R.id.heart2);
            comment2 = itemView.findViewById(R.id.comment2);
            heartCount2 = itemView.findViewById(R.id.heartCount2);
            CommentCount2 = itemView.findViewById(R.id.commentCount2);
            share2 = itemView.findViewById(R.id.share2);
            User_image = itemView.findViewById(R.id.userProfileImg2);
            delect = itemView.findViewById(R.id.delect);
            pipDateAndTime = itemView.findViewById(R.id.pipDateAndTime);
            CurrentUserPipDataImage = itemView.findViewById(R.id.CurrentUserPipDataImage);
        }
    }
}
