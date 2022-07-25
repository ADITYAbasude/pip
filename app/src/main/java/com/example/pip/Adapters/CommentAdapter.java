package com.example.pip.Adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pip.Models.CommentModel;
import com.example.pip.R;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.commentViewHolder> {

    ArrayList<CommentModel> commentData;
    Context context;

    public CommentAdapter(Context context, ArrayList<CommentModel> commentData) {
        this.context = context;
        this.commentData = commentData;
    }

    @NonNull
    @Override
    public CommentAdapter.commentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.showcomments, parent, false);
        return new commentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull commentViewHolder holder, int position) {

        CommentModel comment = commentData.get(position);
        holder.setUserNameInUserReplySection.setText(comment.UserName);
        holder.setUserPipDataInUserReplySection.setText(comment.userComment);

        int modeFlag = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (modeFlag == Configuration.UI_MODE_NIGHT_YES) {
            holder.setUserNameInUserReplySection.setTextColor(Color.WHITE);
            holder.setUserPipDataInUserReplySection.setTextColor(Color.WHITE);
        }

        if (comment.Image_uri != null){
            Glide.with(context).load(comment.Image_uri).into(holder.UserImageInCommentSection);

        } else {
            holder.UserImageInCommentSection.setImageResource(R.drawable.usermodel);
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
