package com.san.ripenessidentification.Common.LogInSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.san.ripenessidentification.R;

public class ForgetPasswordSuccess extends AppCompatActivity {

    Button loginreset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_success);

        loginreset = findViewById(R.id.loginreset_btn);
    }
    public void loginreset_btn(View view){
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }
}