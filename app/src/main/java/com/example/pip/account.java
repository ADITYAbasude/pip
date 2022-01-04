package com.example.pip;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


public class account extends AppCompatActivity {
    private Button  createAccount;
    private TextView loginbtn;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginbtn = findViewById(R.id.loginBtn);
        createAccount = findViewById(R.id.createAccount);
        auth = FirebaseAuth.getInstance();

        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse )
                    {
                        finishAffinity();
                        System.exit(0);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();

        getSupportActionBar().hide();
        loginbtn.setOnClickListener(view -> {
            Intent loginpage = new Intent(this, loginActivity.class);
            startActivity(loginpage);

        });
        createAccount.setOnClickListener(view -> {
            Intent accountcreate = new Intent(this , AccountCreate.class);
            startActivity(accountcreate);
        });

        if (auth.getCurrentUser() != null){
            Intent mainactivity = new Intent(this , pip_splash_screen.class);
            startActivity(mainactivity);
            finish();
        }



    }

}