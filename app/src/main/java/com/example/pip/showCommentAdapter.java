package com.example.pip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class showCommentAdapter extends RecyclerView.Adapter<showCommentAdapter.commentViewHolder> {

    ArrayList<User> commentData;
    Context context;



    public showCommentAdapter(Context context, ArrayList<User> commentData) {
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

        User user = commentData.get(position);
        holder.setUserNameInUserReplySection.setText(user.commentPostUserName);
        holder.setUserPipDataInUserReplySection.setText(user.usercomments);


    }

    @Override
    public int getItemCount() {
        return commentData.size();
    }

    public static class commentViewHolder extends RecyclerView.ViewHolder {
        TextView setUserNameInUserReplySection , setUserPipDataInUserReplySection;
        public commentViewHolder(@NonNull View itemView) {
            super(itemView);
            setUserNameInUserReplySection = itemView.findViewById(R.id.setUserNameInUserReplySection);
            setUserPipDataInUserReplySection = itemView.findViewById(R.id.setUserPipDataInUserReplySection);
        }
    }
}
