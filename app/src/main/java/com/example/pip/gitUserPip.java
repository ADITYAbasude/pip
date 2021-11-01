package com.example.pip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class gitUserPip extends ArrayAdapter<User> {
    ArrayList<User> arr;
    public gitUserPip(@NonNull Context context, int resource, @NonNull ArrayList<User> arr ){

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
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.pipusername,parent,false);
        setTextPipUser = convertView.findViewById(R.id.UserPipName);
        User user = arr.get(position);
        setTextPipUser.setText(user.usName);
        return convertView;
    }
}
