package com.example.pip;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class homePagePipAdapter extends RecyclerView.Adapter<homePagePipAdapter.MyViewAdapter> {
    ArrayList<User> pipDate;
    Context context;
    public final DatabaseReference userPipDataRef = FirebaseDatabase.getInstance().getReference("user").child("UserPost").child("UserPipData");
    public final DatabaseReference userDataRef = FirebaseDatabase.getInstance().getReference("user").child("UserInfo");
    private String uid;


    public homePagePipAdapter(Context context, ArrayList<User> pipDate) {
        this.context = context;
        this.pipDate = pipDate;
    }


    @NonNull
    @Override
    public homePagePipAdapter.MyViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.home_page_recycler, parent, false);
        return new MyViewAdapter(v);
    }

    @Override
    public void onBindViewHolder(@NonNull homePagePipAdapter.MyViewAdapter holder, int position) {
        User user = pipDate.get(position);
        holder.userPipName.setText(user.pipuserName);
        holder.pipDateShow.setText(user.pipPostData);
        holder.setDate.setText(user.date);
//
        userDataRef.orderByChild("usName").equalTo(user.pipuserName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                uid = snapshot.getRef().getKey();

                setPipImage(uid, user, holder.setPipImageData);
                snapshot.getRef().child("Profile_Image").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            User user1 = snapshot.getValue(User.class);
                            Glide.with(context).load(Uri.parse(user1.User_Profile_Image_Uri)).into(holder.setUserImage);
                        } else {
                            holder.setUserImage.setImageResource(R.drawable.ic_baseline_account_circle_24);
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
        recyclerClickView(holder.setClick, user);
    }

    public void recyclerClickView(ConstraintLayout setClick, User user) {
        setClick.setOnClickListener(v -> {
            Intent intent = new Intent(context, pip_view.class);
            intent.putExtra("pip_id", user.pip_id);
            intent.putExtra("name", user.pipuserName);
            context.startActivity(intent);
        });
    }


    private void setPipImage(String uid, User user, ImageView setPipImageData) {
        userPipDataRef.child(uid).child(user.pip_id).child("ImageUriFromDatabase").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    setPipImageData.setVisibility(View.VISIBLE);
                    pipDataImageModel imageModel = snapshot.getValue(pipDataImageModel.class);
                    Glide.with(context).load(Uri.parse(imageModel.pipImageData)).into(setPipImageData);
                } else {
                    setPipImageData.setImageResource(R.drawable.ic_baseline_home_24);
                    setPipImageData.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return pipDate.size();
    }

    public static class MyViewAdapter extends RecyclerView.ViewHolder {

        TextView userPipName, pipDateShow, setDate;
        ImageView setUserImage, setPipImageData;
        ConstraintLayout setClick;

        public MyViewAdapter(@NonNull View itemView) {
            super(itemView);

            userPipName = itemView.findViewById(R.id.name);
            pipDateShow = itemView.findViewById(R.id.pip);
            setUserImage = itemView.findViewById(R.id.photo);
            setPipImageData = itemView.findViewById(R.id.setImage);
            setDate = itemView.findViewById(R.id.dateTime3);
            setClick = itemView.findViewById(R.id.setClick);
        }


    }

}
