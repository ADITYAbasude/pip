package com.example.pip;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class gitUserPip extends ArrayAdapter<User> {
    ArrayList<User> arr;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public gitUserPip(@NonNull Context context, int resource, @NonNull ArrayList<User> arr) {

        super(context, resource, arr);

        this.arr = arr;
    }

    @Nullable
    @Override
    public User getItem(int position) {
        return arr.get(position);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView setTextPipUser, followBtn;
        ArrayList<String> storeFollowingUID = new ArrayList<>();


        convertView = LayoutInflater.from(getContext()).inflate(R.layout.pipusername, parent, false);
        setTextPipUser = convertView.findViewById(R.id.UserPipName);
        followBtn = convertView.findViewById(R.id.followBtn);
        User user = arr.get(position);
        setTextPipUser.setText(user.usName);
        followBtn.setOnClickListener(view -> {
            if (followBtn.getText().toString() == "Following") {

                followBtn.setText("Follow");

            } else {
                following(storeFollowingUID, user);
                FirebaseDatabase.getInstance().getReference("user").child("UserInfo").child(uid)
                        .child("Following").push().setValue(storeFollowingUID);

            }
        });


        return convertView;
    }


    private void following(ArrayList<String> storeFollowingUID, User user) {

        FirebaseDatabase.getInstance().getReference("user").child("UserInfo").orderByChild("usName").equalTo(user.usName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                storeFollowingUID.add(snapshot.getKey());
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


}
