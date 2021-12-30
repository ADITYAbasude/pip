package com.example.pip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class settingsAnsPrivercy extends AppCompatActivity {
    private TextView aboutApp , feedback ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_ans_privercy);
        getSupportActionBar().hide();

        aboutApp = findViewById(R.id.AboutApp);
        feedback = findViewById(R.id.feedBack);


        Window window  = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this  , R.color.purple_500));

        aboutApp.setOnClickListener(v -> {
            Intent intent = new Intent(this, aboutapp.class);
            startActivity(intent);
        });

        feedback.setOnClickListener(v-> {
            Intent intent = new Intent(this, feedback.class);
            startActivity(intent);
        });



    }
}