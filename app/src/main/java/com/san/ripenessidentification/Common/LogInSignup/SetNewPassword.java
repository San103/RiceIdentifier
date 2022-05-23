package com.san.ripenessidentification.Common.LogInSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.san.ripenessidentification.R;

public class SetNewPassword extends AppCompatActivity {

    ProgressBar progressBar;
    TextInputLayout newPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);
        progressBar = findViewById(R.id.progress_bar);
        newPass = findViewById(R.id.ReEnterNewPassword);
    }

    public void UpdateNewPass(View view) {
        progressBar.setVisibility(View.VISIBLE);

        String newPassword = newPass.getEditText().getText().toString().trim();
        String _phoneNumber = getIntent().getStringExtra("phoneNo");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(_phoneNumber).child("password").setValue(newPassword);

        startActivity(new Intent(getApplicationContext(), ForgetPasswordSuccess.class));
        finish();
    }
}