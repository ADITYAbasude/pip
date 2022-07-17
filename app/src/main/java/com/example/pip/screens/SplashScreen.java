package com.example.pip.screens;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.pip.R;

public class SplashScreen extends AppCompatActivity {

    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pip_splash_screen);

        ActionBar action = getSupportActionBar();
        action.hide();

        handler.postDelayed(() -> {
            Intent i = new Intent(SplashScreen.this , HomeScreen.class);
            startActivity(i);
            finish();
        }, 1000);

    }
}