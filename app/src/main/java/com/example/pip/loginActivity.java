package com.example.pip;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class loginActivity extends AppCompatActivity {
    Button logbtn;
    EditText useridtaken, passwordtaken;
    FirebaseAuth cerateaccountdata;
    ProgressBar progressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("Login Account");

        setContentView(R.layout.activity_login);
        cerateaccountdata = FirebaseAuth.getInstance();
        logbtn = findViewById(R.id.loginBtn);
        useridtaken = findViewById(R.id.userNameAndId);
        passwordtaken = findViewById(R.id.userpasswordId);
        progressBar2 = findViewById(R.id.progressBar2);

        logbtn.setOnClickListener(view -> {
            String ust = useridtaken.getText().toString().trim();
            String pt = passwordtaken.getText().toString().trim();
            progressBar2.setVisibility(view.VISIBLE);
            if (pt.isEmpty()) {
                passwordtaken.setError("Fill the passwords");
                passwordtaken.requestFocus();
                progressBar2.setVisibility(view.GONE);
            } else if (ust.isEmpty()) {
                useridtaken.setError("Fill the passwords");
                useridtaken.requestFocus();
                progressBar2.setVisibility(view.GONE);
            } else if (pt.length() <= 7) {
                passwordtaken.setError("Your password must be 8 (Letters , Numbers , and other) mixing");
                passwordtaken.requestFocus();
                progressBar2.setVisibility(view.GONE);
            } else if (!pt.isEmpty() && !ust.isEmpty()) {

                cerateaccountdata.signInWithEmailAndPassword(ust, pt).addOnCompleteListener(loginActivity.this, task -> {

                    if (!task.isSuccessful()) {
                        Toast.makeText(loginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        progressBar2.setVisibility(view.GONE);
                    } else {
                        Intent twitpagegone2 = new Intent(loginActivity.this, twitpage.class);
                        startActivity(twitpagegone2);
                        progressBar2.setVisibility(view.GONE);
                    }
                });
            }
            else {

                progressBar2.setVisibility(view.GONE);
            }

        });






    }
}