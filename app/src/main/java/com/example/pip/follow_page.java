package com.example.pip;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.util.List;

public class follow_page extends RecyclerView.Adapter<follow_page.MyViewAdapter> {
    ArrayList<User> arr;
    ArrayList<User> Store_User_Img;
    Context context;
    Uri take_Uri;

    public follow_page(Context context, ArrayList<User> arr, ArrayList<User> Store_User_Img) {
        this.context = context;
        this.arr = arr;
        this.Store_User_Img = Store_User_Img;
    }

    @NonNull
    @Override
    public MyViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.pipusername, parent, false);
        return new MyViewAdapter(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewAdapter holder, int position) {
        User user = arr.get(position);
        holder.setTextPipUser.setText(user.usName);
        FirebaseDatabase.getInstance().getReference("user").child("UserInfo").orderByChild("usName").equalTo(user.usName)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        snapshot.getRef().child("Profile_Image").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    User Img_Link = snapshot.getValue(User.class);
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


    }


    @Override
    public int getItemCount() {
        return arr.size();
    }


    public class MyViewAdapter extends RecyclerView.ViewHolder {
        TextView setTextPipUser, followBtn;
        ImageView set_User_Image;

        public MyViewAdapter(@NonNull View itemView) {
            super(itemView);

            setTextPipUser = itemView.findViewById(R.id.UserPipName);
            set_User_Image = itemView.findViewById(R.id.set_User_img);
//            followBtn = itemView.findViewById(R.id.followBtn);

        }
    }
//


//    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//
//        ArrayList<String> storeFollowingUID = new ArrayList<>();


//        convertView = LayoutInflater.from(getContext()).inflate(R.layout.pipusername, parent, false);


//        Picasso.get().load(user1.User_Profile_Image_Uri).into(set_User_Image);


//        followBtn.setOnClickListener(view -> {
//            if (followBtn.getText().toString() == "Following") {
//
//                followBtn.setText("Follow");
//
//            } else {
//                following(storeFollowingUID, user);
//                FirebaseDatabase.getInstance().getReference("user").child("UserInfo").child(uid)
//                        .child("Following").push().setValue(storeFollowingUID);
//
//            }
//        });


//        FirebaseDatabase.getInstance().getReference().child("user").child("UserInfo").orderByChild(user.usName)
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()) {
//
//                                                    } else {
//                            set_User_Image.setImageResource(R.drawable.ic_baseline_account_circle_24);
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

//        return convertView;
//    }


//    private void following(ArrayList<String> storeFollowingUID, User user) {
//
//        FirebaseDatabase.getInstance().getReference("user").child("UserInfo").orderByChild("usName").equalTo(user.usName).addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                storeFollowingUID.add(snapshot.getKey());
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }


}
