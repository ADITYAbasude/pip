package com.example.pip;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class home extends Fragment {
    private FloatingActionButton addTwitBtn;
    public home() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // ----------------------------id decleration ------------------------

        addTwitBtn = view.findViewById(R.id.floatingActionButtonforTwit);


//        ----------click on floating btn for twit --------------

        addTwitBtn.setOnClickListener(view1 -> {
            Intent userTwitPageOpen = new Intent(getContext() , twittingpageUser.class);
            startActivity(userTwitPageOpen);
        });


    }
}