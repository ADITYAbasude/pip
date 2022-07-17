package com.example.pip.screens;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.pip.R;
import com.example.pip.Models.FeedbackModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class FeedbackScreen extends AppCompatActivity {
    EditText feedbackText;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        Window window  = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this  , R.color.main_color));
        ActionBar bar;
        bar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#ffffff"));
        bar.setBackgroundDrawable(colorDrawable);
        bar.setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow =  ContextCompat.getDrawable(this, R.drawable.arrow_back);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_ATOP);
        bar.setHomeAsUpIndicator(upArrow);
        bar.setElevation(5);
        bar.setTitle(Html.fromHtml("<font color='#000000'>Feedback</font>"));

        feedbackText = findViewById(R.id.feedbackText);
        submit = findViewById(R.id.submitBtn);




        submit.setOnClickListener(v -> {
            FeedbackModel model = new FeedbackModel(FirebaseAuth.getInstance().getUid() , feedbackText.getText().toString());
            FirebaseDatabase.getInstance().getReference("user").child("feedbacks")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(model).addOnCompleteListener(task -> {
                        feedbackText.setText("");
                    });
        });
    }
}