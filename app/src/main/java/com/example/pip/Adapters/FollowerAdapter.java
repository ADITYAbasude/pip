package com.example.pip.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pip.R;
import com.example.pip.Models.UserModel;
import com.example.pip.screens.StartScreen;
import com.example.pip.screens.VisitOtherUserProfileScreen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class FollowerAdapter extends RecyclerView.Adapter<FollowerAdapter.ViewAdapter> {
    private Context context;
    private List<String> pipData;
    private DatabaseReference userDataRef = FirebaseDatabase.getInstance().getReference("user").child("UserInfo");
    private String storeName;

    public FollowerAdapter(Context context, List<String> pipDate) {
        this.context = context;
        this.pipData = pipDate;
    }

    @NonNull
    @Override
    public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.pipusername, parent, false);

        return new FollowerAdapter.ViewAdapter(v);
    }

    @NonNull


    @Override
    public void onBindViewHolder(@NonNull ViewAdapter holder, int position) {
        String a = pipData.get(position);
        userDataRef.child(a).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    UserModel userModel = snapshot.getValue(UserModel.class);
                    holder.userName.setText(userModel.usName);
                    storeName = userModel.usName;
                } else {
                    Intent intent = new Intent(context, StartScreen.class);
                    context.startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userDataRef.child(a).child("Profile_Image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    UserModel userModel = snapshot.getValue(UserModel.class);
                    Glide.with(context).load(Uri.parse(userModel.User_Profile_Image_Uri)).into(holder.set_User_Image);
                } else {
                    holder.set_User_Image.setImageResource(R.drawable.usermodel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.Visit_profile.setOnClickListener(v -> {

            Intent intent = new Intent(context, VisitOtherUserProfileScreen.class);
            Toast.makeText(context, storeName, Toast.LENGTH_SHORT).show();
            intent.putExtra("Uid", storeName);
            context.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewAdapter extends RecyclerView.ViewHolder {
        TextView userName;
        ImageView set_User_Image;
        Button Visit_profile;
        public ViewAdapter(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.UserPipName);
            set_User_Image = itemView.findViewById(R.id.set_User_img);
            Visit_profile = itemView.findViewById(R.id.Visit_profile);
        }
    }
}
