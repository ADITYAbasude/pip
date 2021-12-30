package com.example.pip;

import android.annotation.SuppressLint;
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
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewAdapter> {
    ArrayList<User> arr;
    Context context;
    Uri take_Uri;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String takeSnapshotRef, takeUserId;

    public SearchAdapter(Context context, ArrayList<User> arr) {
        this.context = context;
        this.arr = arr;
    }

    @NonNull
    @Override
    public MyViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.pipusername, parent, false);
        return new MyViewAdapter(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewAdapter holder, int position) {
        User user = arr.get(position);
        holder.setTextPipUser.setText(user.usName);
        FirebaseDatabase.getInstance().getReference("user").child("UserInfo").orderByChild("usName").equalTo(user.usName)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        takeSnapshotRef = snapshot.getKey();
                        snapshot.getRef().child("Profile_Image").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                if (snapshot1.exists()) {
                                    User Img_Link = snapshot1.getValue(User.class);
                                    take_Uri = Uri.parse(Img_Link.User_Profile_Image_Uri);
                                    Glide.with(context).load(take_Uri).into(holder.set_User_Image);
                                } else {
                                    holder.set_User_Image.setImageResource(R.drawable.ic_baseline_account_circle_24);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
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

        Visit_profile_btn_invoke(holder.Visit_profile, user);


    }

    private void Visit_profile_btn_invoke(Button Visit_profile, User user) {

        Visit_profile.setOnClickListener(v -> {
            FirebaseDatabase.getInstance().getReference("user").child("UserInfo").orderByChild("usName").equalTo(user.usName).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (!snapshot.getRef().getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        Intent intent = new Intent(context, Visit_Another_profile.class);
                        intent.putExtra("Uid", user.usName);
                        context.startActivity(intent);
                    }else{
                        Intent intent = new Intent(context, User_profile_visit.class);
                        context.startActivity(intent);
                    }
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


        });
    }


    @Override
    public int getItemCount() {
        return arr.size();
    }


    public class MyViewAdapter extends RecyclerView.ViewHolder {
        TextView setTextPipUser;
        Button Visit_profile;
        ImageView set_User_Image;

        public MyViewAdapter(@NonNull View itemView) {
            super(itemView);

            setTextPipUser = itemView.findViewById(R.id.UserPipName);
            set_User_Image = itemView.findViewById(R.id.set_User_img);
            Visit_profile = itemView.findViewById(R.id.Visit_profile);

        }
    }


}
