package com.san.ripenessidentification.Common.LogInSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.san.ripenessidentification.R;

import java.util.EventListener;

public class forgot_password extends AppCompatActivity {

    ImageView back;
    TextInputLayout forgetfield;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        forgetfield = findViewById(R.id.numberReset);
        back= findViewById(R.id.backforgot_btn);
    }
    public void back_btn(View view){
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }
    public void resetpasswordbtn(View view){

        String number = forgetfield.getEditText().getText().toString().trim();

        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("number").equalTo(number);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                if (dataSnapshot.exists()){
                    Intent intent = new Intent(getApplicationContext(), Verify_OTPforgot.class);
                    intent.putExtra("phoneNo", number);
                    intent.putExtra("forextension", "updateData");
                    startActivity(intent);
                    finish();

                }else{
                    Toast.makeText(forgot_password.this, "Data Not Exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
}