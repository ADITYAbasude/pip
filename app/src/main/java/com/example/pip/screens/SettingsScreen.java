package com.example.pip.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.example.pip.R;
import com.example.pip.screens.AboutScreen;
import com.example.pip.screens.FeedbackScreen;

public class SettingsScreen extends AppCompatActivity {
    private TextView aboutApp , feedback ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_ans_privercy);
        getSupportActionBar().hide();

        aboutApp = findViewById(R.id.AboutApp);
        feedback = findViewById(R.id.feedBack);


        Window window  = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this  , R.color.main_color));

        aboutApp.setOnClickListener(v -> {
            Intent intent = new Intent(this, AboutScreen.class);
            startActivity(intent);
        });

        feedback.setOnClickListener(v-> {
            Intent intent = new Intent(this, FeedbackScreen.class);
            startActivity(intent);
        });

    }
}