package com.example.pip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class gitUserPip extends ArrayAdapter<User> {
    ArrayList<User> arr;
    boolean followOrNot;
    public gitUserPip(@NonNull Context context, int resource, @NonNull ArrayList<User> arr) {

        super(context, resource, arr);

        this.arr = arr;
    }

    @Nullable
    @Override
    public User getItem(int position) {
        return arr.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView setTextPipUser;
        Button followBtn;

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.pipusername, parent, false);
        setTextPipUser = convertView.findViewById(R.id.UserPipName);
        followBtn = convertView.findViewById(R.id.followBtn);
        User user = arr.get(position);
        setTextPipUser.setText(user.usName);
        followBtn.setOnClickListener(view -> {
            if (followBtn.getText() == "Unfollow") {
                followBtn.setText("Follow");
                followOrNot = false;

            } else {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    followBtn.setText("Unfollow");
                    String takeUserNameFromSearch = setTextPipUser.getText().toString();

                    FirebaseDatabase.getInstance().getReference("user").child("UserInfo")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("Followers").push().setValue(takeUserNameFromSearch);
                    followOrNot = true;
                }
            }
        });
        return convertView;
    }


}
