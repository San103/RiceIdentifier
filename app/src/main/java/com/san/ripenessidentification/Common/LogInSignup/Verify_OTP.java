package com.san.ripenessidentification.Common.LogInSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.san.ripenessidentification.Databases.UserHelperClass;
import com.san.ripenessidentification.MainActivity;
import com.san.ripenessidentification.R;
import com.san.ripenessidentification.User.UserDashboard;

import java.util.concurrent.TimeUnit;

public class Verify_OTP extends AppCompatActivity {

    Button verify_btn;
    TextView DetailsVerifiy;
    EditText numberInputed;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    String verificationCodeBySystem;
    private String verificationId;
    String name, username, pass, phoneNo, phone, forextension;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify__o_t_p);

        mAuth = FirebaseAuth.getInstance();

        verify_btn = findViewById(R.id.verify_btn);
        numberInputed = findViewById(R.id.numberinputedbyuser);
        progressBar = findViewById(R.id.progress_bar);
        DetailsVerifiy = findViewById(R.id.desVerification);


        name = getIntent().getStringExtra("fullname");
        username = getIntent().getStringExtra("username");
        pass = getIntent().getStringExtra("pass");
        phoneNo = getIntent().getStringExtra("phoneNo");
        forextension = getIntent().getStringExtra("forextension");

        phone = "+63" + phoneNo;
        DetailsVerifiy.setText("Enter the One-Time Verification Code sent on " + phone);
        sendVerificationCode(phone);
    }

    public void resend_btn(View view) {
        sendVerificationCode(phone);
    }

    public void verifycode_btn(View view) {
        if (TextUtils.isEmpty(numberInputed.getText().toString())) {
            Toast.makeText(Verify_OTP.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
        } else {
            verifyCode(numberInputed.getText().toString());
            storeNewUsersData();
        }
    }

    private void updateOldUsersData() {
        Intent intent = new Intent(getApplicationContext(), SetNewPassword.class);
        intent.putExtra("phoneNo", phoneNo);
        startActivity(intent);
        finish();
    }

    private void storeNewUsersData() {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Users");

        UserHelperClass addNewUser = new UserHelperClass(name, username, phoneNo, pass, "Premium", "User", "https://firebasestorage.googleapis.com/v0/b/ripeness-identifier.appspot.com/o/1652775719421.png?alt=media&token=35a63ff5-cc11-4cc7-835a-9fc749f1218a", 0);
        reference.child(phoneNo).setValue(addNewUser);
    }

    public void savedata(String username, String fname, String number){
        SharedPreferences sharedPreferences=getSharedPreferences("logindata",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("logincounter",true);
        editor.putString("username",username);
        editor.putString("fullname",fname);
        editor.putString("number",number);
        editor.putString("sid", number);
        editor.putString("imgUrl","https://firebasestorage.googleapis.com/v0/b/ripeness-identifier.appspot.com/o/1652775719421.png?alt=media&token=35a63ff5-cc11-4cc7-835a-9fc749f1218a");
        editor.apply();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        setLoginState(false);
        intent.putExtra("fullname", fname);
        intent.putExtra("number", number);
        startActivity(intent);
    }
    private void setLoginState(boolean status) {
        SharedPreferences sp = getSharedPreferences("logindata2",
                MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("setLoggingOut", status);
        ed.commit();
    }

    private void signInWithCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

//                            Intent i = new Intent(Verify_OTP.this, MainActivity.class);
                            savedata(username,name,phoneNo);
//                            startActivity(i);
                            finish();

                        } else {
                            Toast.makeText(Verify_OTP.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String number) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallBack)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            final String code = phoneAuthCredential.getSmsCode();

            if (code != null) {
                numberInputed.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Verify_OTP.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }


}