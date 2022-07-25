package com.example.pip.auth.screens;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.pip.R;
import com.example.pip.databinding.ActivityLoginUserBinding;
import com.example.pip.screens.HomeScreen;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginUserScreen extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    private ActivityLoginUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();

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

        binding.loginBtn.setOnClickListener(view -> {
            String ust = Objects.requireNonNull(binding.takeEmailForLogin.getText()).toString().trim();
            String pt = Objects.requireNonNull(binding.takePasswordForLogin.getText()).toString().trim();
            if (pt.isEmpty()) {
                binding.takePasswordForLogin.setError("Fill the passwords");
                binding.takePasswordForLogin.requestFocus();
            } else if (ust.isEmpty()) {
                binding.takeEmailForLogin.setError("Fill the passwords");
                binding.takeEmailForLogin.requestFocus();
            } else if (!ust.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                binding.takeEmailForLogin.setError("Enter a valid email");
                binding.takeEmailForLogin.requestFocus();
            } else if (pt.length() <= 7) {
                binding.takePasswordForLogin.setError("Your password must be 8 (Letters , Numbers , and other) mixing");
                binding.takePasswordForLogin.requestFocus();
            } else {
                binding.progressBar2.setVisibility(View.VISIBLE);
                firebaseAuth.signInWithEmailAndPassword(ust, pt).addOnCompleteListener(LoginUserScreen.this, task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(LoginUserScreen.this, "Failed", Toast.LENGTH_SHORT).show();
                        binding.progressBar2.setVisibility(View.GONE);
                    } else {
                        Intent intent = new Intent(LoginUserScreen.this, HomeScreen.class);
                        startActivity(intent);
                        binding.progressBar2.setVisibility(View.GONE);
                    }
                });
            }

        });


    }

    void setUiAsNightMode() {
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.arrow_back);
        assert upArrow != null;
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setTitle("Login your account");
    }

    void setUiAsLightMode() {
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.arrow_back);
        assert upArrow != null;
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_ATOP);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'>Login your account</font>"));
    }
}