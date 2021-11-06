package com.example.pip;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class homePagePipAdapter extends RecyclerView.Adapter<homePagePipAdapter.MyViewAdapter> {
    ArrayList<User> pipDate;
    Context context;

    int countLike;


    public homePagePipAdapter(Context context, ArrayList<User> pipDate) {
        this.context = context;
        this.pipDate = pipDate;
    }


    @NonNull
    @Override
    public homePagePipAdapter.MyViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.homepagetoseepip, parent, false);
        return new MyViewAdapter(v);
    }

    @Override
    public void onBindViewHolder(@NonNull homePagePipAdapter.MyViewAdapter holder, int position) {
        User user = pipDate.get(position);
        ImageView heart = holder.heart;

        holder.userPipName.setText(user.pipuserName);
        holder.pipDateShow.setText(user.pipPostData);
        holder.heartCount.setText(user.like);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            try {
                heart.setOnClickListener(view -> {
                    holder.likeCount = user.like;
                    holder.parseInIntegerLikeCount = Integer.parseInt(holder.likeCount);
                    countLike = holder.parseInIntegerLikeCount;

                    if (holder.checkLikeOrNot == false) {
                        if (countLike != 0) {
                            heart.setImageResource(R.drawable.heart);
                            countLike--;
                            holder.checkLikeOrNot = true;
                            holder.heartCount.setText(String.valueOf(countLike));
                            likeMinesMines(user);

                        }
                    } else {
                        heart.setImageResource(R.drawable.heartred);
                        countLike++;
                        likePlusPlus(user);
                        holder.heartCount.setText(String.valueOf(countLike));
                        holder.checkLikeOrNot = false;
                    }

                });
            } catch (Exception e) {
                Toast.makeText(context, "Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Please login", Toast.LENGTH_SHORT).show();
        }

        holder.comment.setOnClickListener(view->{
            Intent moveCommentPage = new Intent(context , userComment.class);
            moveCommentPage.putExtra("userName" , holder.userPipName.getText().toString());
            moveCommentPage.putExtra("pipData" , holder.pipDateShow.getText().toString());
            context.startActivity(moveCommentPage);

        });
//        ifUserClickOnComment(holder.comment);

    }


    private void likeMinesMines(User user) {
        FirebaseDatabase.getInstance().getReference("user").child("All-User-Pip-Data").orderByChild("pipPostData")
                .equalTo(user.pipPostData)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ds.getRef().child("like").setValue(String.valueOf(countLike));
                        }
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void likePlusPlus(User user) {
        FirebaseDatabase.getInstance().getReference("user").child("All-User-Pip-Data").orderByChild("pipPostData")
                .equalTo(user.pipPostData)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ds.getRef().child("like").setValue(String.valueOf(countLike));
                        }
                        notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }


    private void ifUserClickOnComment(ImageView comment){

    }


    @Override
    public int getItemCount() {
        return pipDate.size();
    }

    public static class MyViewAdapter extends RecyclerView.ViewHolder {

        TextView userPipName, pipDateShow, heartCount, CommentCount;
        ImageView heart  , comment;
        String likeCount;
        int parseInIntegerLikeCount;
        boolean checkLikeOrNot = true;

        public MyViewAdapter(@NonNull View itemView) {
            super(itemView);

            userPipName = itemView.findViewById(R.id.UserNameInPip);
            pipDateShow = itemView.findViewById(R.id.PipPostDate);
            heart = itemView.findViewById(R.id.heart);
            comment = itemView.findViewById(R.id.comment);
            heartCount = itemView.findViewById(R.id.heartCount);
            CommentCount = itemView.findViewById(R.id.commentCount);


        }


    }

}
