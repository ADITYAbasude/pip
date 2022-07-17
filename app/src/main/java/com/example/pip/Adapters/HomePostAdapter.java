package com.example.pip.Adapters;

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
import com.example.pip.R;
import com.example.pip.Models.UserModel;
import com.example.pip.Models.PostDataImageModel;
import com.example.pip.screens.PostViewScreen;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomePostAdapter extends RecyclerView.Adapter<HomePostAdapter.MyViewAdapter> {
    ArrayList<UserModel> pipDate;
    Context context;
    public final DatabaseReference userPipDataRef = FirebaseDatabase.getInstance().getReference("user").child("UserPost").child("UserPipData");
    public final DatabaseReference userDataRef = FirebaseDatabase.getInstance().getReference("user").child("UserInfo");
    private String uid;


    public HomePostAdapter(Context context, ArrayList<UserModel> pipDate) {
        this.context = context;
        this.pipDate = pipDate;
    }


    @NonNull
    @Override
    public HomePostAdapter.MyViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.home_page_recycler, parent, false);
        return new MyViewAdapter(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HomePostAdapter.MyViewAdapter holder, int position) {
        UserModel userModel = pipDate.get(position);
        holder.userPipName.setText(userModel.pipuserName);
        holder.pipDateShow.setText(userModel.pipPostData);
        holder.setDate.setText(userModel.date);
//
        userDataRef.orderByChild("usName").equalTo(userModel.pipuserName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                uid = snapshot.getRef().getKey();

                setPipImage(uid, userModel, holder.setPipImageData);
                snapshot.getRef().child("Profile_Image").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            UserModel userModel1 = snapshot.getValue(UserModel.class);
                            Glide.with(context).load(Uri.parse(userModel1.User_Profile_Image_Uri)).into(holder.setUserImage);
                        } else {
                            holder.setUserImage.setImageResource(R.drawable.usermodel);
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
        recyclerClickView(holder.setClick, userModel);
    }

    public void recyclerClickView(ConstraintLayout setClick, UserModel userModel) {
        setClick.setOnClickListener(v -> {
            Intent intent = new Intent(context, PostViewScreen.class);
            intent.putExtra("pip_id", userModel.pip_id);
            intent.putExtra("name", userModel.pipuserName);
            context.startActivity(intent);
        });
    }


    private void setPipImage(String uid, UserModel userModel, ImageView setPipImageData) {
        userPipDataRef.child(uid).child(userModel.pip_id).child("ImageUriFromDatabase").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    setPipImageData.setVisibility(View.VISIBLE);
                    PostDataImageModel imageModel = snapshot.getValue(PostDataImageModel.class);
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
