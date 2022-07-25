package com.example.pip.user.settings;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.pip.R;
import com.example.pip.databinding.ActivitySettingsAndPrivercyBinding;

import java.util.Objects;

public class SettingsScreen extends AppCompatActivity {
    private ActivitySettingsAndPrivercyBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsAndPrivercyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();



        int modeFlag = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (modeFlag) {
            case Configuration.UI_MODE_NIGHT_YES:
                setUiAsNightMode();
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                setUiAsLightMode();
                break;
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                setUiAsLightMode();
                break;
        }

        binding.AboutApp.setOnClickListener(v -> {
            Intent intent = new Intent(this, AboutScreen.class);
            startActivity(intent);
        });

        binding.feedBack.setOnClickListener(v-> {
            Intent intent = new Intent(this, FeedbackScreen.class);
            startActivity(intent);
        });

        binding.closeFeedBackScreen.setOnClickListener(view-> onBackPressed());

    }

    void setUiAsNightMode() {
        final Drawable close = ContextCompat.getDrawable(this, R.drawable.round_close);
        assert close != null;
        close.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        binding.closeFeedBackScreen.setImageDrawable(close);
    }

    void setUiAsLightMode() {
        final Drawable close = ContextCompat.getDrawable(this, R.drawable.round_close);
        assert close != null;
        close.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_ATOP);
        binding.closeFeedBackScreen.setImageDrawable(close);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        startActivity(new Intent(this , HomeScreen.class));
        overridePendingTransition(R.anim.activity_in , R.anim.activity_out);
    }
}