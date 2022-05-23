package com.san.ripenessidentification.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.san.ripenessidentification.Common.LogInSignup.Login;
import com.san.ripenessidentification.R;

public class UserDashboard extends AppCompatActivity {
    FirebaseAuth mAuth;
    Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard2);
        mAuth =FirebaseAuth.getInstance();
        logout=findViewById(R.id.logout_btn);

    }
    public void log_out(View v){
        mAuth.signOut();
        startActivity(new Intent(UserDashboard.this, Login.class));
        finish();
    }
}