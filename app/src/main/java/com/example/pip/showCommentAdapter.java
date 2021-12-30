package com.example.pip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class showCommentAdapter extends RecyclerView.Adapter<showCommentAdapter.commentViewHolder> {

    ArrayList<comment> commentData;
    Context context;



    public showCommentAdapter(Context context, ArrayList<comment> commentData) {
        this.context = context;
        this.commentData = commentData;
    }



    @NonNull
    @Override
    public showCommentAdapter.commentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.showcomments, parent, false);
        return new commentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull commentViewHolder holder, int position) {

        comment comment = commentData.get(position);
        holder.setUserNameInUserReplySection.setText(comment.UserName);
        holder.setUserPipDataInUserReplySection.setText(comment.userComment);

        if (comment.Image_uri != null){
            Glide.with(context).load(comment.Image_uri).into(holder.UserImageInCommentSection);

        } else {
            holder.UserImageInCommentSection.setImageResource(R.drawable.ic_baseline_account_circle_24);
        }


    }

    @Override
    public int getItemCount() {
        return commentData.size();
    }

    public static class commentViewHolder extends RecyclerView.ViewHolder {
        TextView setUserNameInUserReplySection , setUserPipDataInUserReplySection;
        ImageView UserImageInCommentSection;
        public commentViewHolder(@NonNull View itemView) {
            super(itemView);
            setUserNameInUserReplySection = itemView.findViewById(R.id.setUserNameInUserReplySection);
            setUserPipDataInUserReplySection = itemView.findViewById(R.id.setUserPipDataInUserReplySection);
            UserImageInCommentSection = itemView.findViewById(R.id.UserImageInCommentSection);
        }
    }
}
