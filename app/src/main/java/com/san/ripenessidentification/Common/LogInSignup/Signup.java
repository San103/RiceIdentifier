package com.san.ripenessidentification.Common.LogInSignup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.san.ripenessidentification.Common.OnBoarding;
import com.san.ripenessidentification.Common.SplashScreen;
import com.san.ripenessidentification.Databases.UserHelperClass;
import com.san.ripenessidentification.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class Signup extends AppCompatActivity {

    TextInputLayout Regname;
    TextInputLayout Regusername;
    TextInputLayout Regnumber;
    TextInputLayout Regpass;
    TextInputLayout Regrepass;
    TextInputEditText RegUsername;
    TextInputEditText RegNumber;
    ImageView back;
    boolean checkuserNumber;
    boolean checkuserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_signup);

        Regname = findViewById(R.id.reg_name);
        Regusername = findViewById(R.id.reg_username);
        Regnumber = findViewById(R.id.reg_number);
        Regpass = findViewById(R.id.reg_pass);
        Regrepass = findViewById(R.id.reg_repass);
        back = findViewById(R.id.signup_back_btn);
        RegUsername = findViewById(R.id.usernameEdit);
        RegNumber = findViewById(R.id.numberEdit);

        Query reference = FirebaseDatabase.getInstance().getReference("Users");

        RegUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Query secondQuery = reference.orderByChild("username").equalTo(String.valueOf(editable));
                secondQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Regusername.setError("USername already Exist");
                            checkuserNumber = false;
                        } else {
                            Regusername.setError(null);
                            checkuserNumber = true;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
            }
        });
        RegNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Query firstQuery = reference.orderByChild("number").equalTo(String.valueOf(editable));
                firstQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Regnumber.setError("Phone already Exist");
                            checkuserName = false;
                        } else {
                            Regnumber.setError(null);
                            checkuserName = true;
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    public void back_btns(View view) {
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    private Boolean validateName() {
        String val = Regname.getEditText().getText().toString();
        if (val.isEmpty()) {
            Regname.setError("Field cannot be empty");
            return false;
        } else {
            Regname.setError(null);
            Regname.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateUserName() {
        String val = Regusername.getEditText().getText().toString();
        String noWHiteSpaces = ".*([ \\t]).*";
        if (val.isEmpty()) {
            Regusername.setError("Field cannot be empty");
            return false;
        } else if (val.length() >= 10) {
            Regusername.setError("Username too long");
            return false;
        } else if (val.matches(noWHiteSpaces)) {
            Regusername.setError("Username has space");
            return false;
        } else {
            Regusername.setError(null);
            Regusername.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePhone() {
        String val = Regnumber.getEditText().getText().toString();
        if (val.isEmpty()) {
            Regnumber.setError("Field cannot be empty");
            return false;
        } else if (val.length() > 10) {
            Regnumber.setError("Number too long");
            return false;
        } else {
            Regnumber.setError(null);
            Regnumber.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = Regpass.getEditText().getText().toString();
        String repass = Regrepass.getEditText().getText().toString();
        if (val.isEmpty()) {
            Regpass.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(repass)) {
            Regpass.setError("Password did not Match");
            return false;

        } else {
            Regpass.setError(null);
            Regpass.setErrorEnabled(false);
            return true;
        }
    }

    public void SignIn(View view) {


        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }


    public void otp_page(View view) {

        if (!validateName() || !validateUserName() || !validatePhone() || !validatePassword() || !checkuserNumber || !checkuserName) {
            if (!checkuserNumber) {
                Regusername.setError("Username already Exist");
            }
            if (!checkuserName) {
                Regnumber.setError("Phone already Exist");
            }
            return;

        } else {
            String name = Regname.getEditText().getText().toString();
            String username = Regusername.getEditText().getText().toString();
            String number = Regnumber.getEditText().getText().toString().trim();
            String pass = Regpass.getEditText().getText().toString();
            Intent intent = new Intent(getApplicationContext(), Verify_OTP.class);
            intent.putExtra("fullname", name);
            intent.putExtra("username", username);
            intent.putExtra("pass", pass);
            intent.putExtra("phoneNo", number);
            startActivity(intent);
//        ChildEventListener childEventListener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                if(dataSnapshot.exists()) {
//                    firstQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            if (snapshot.exists()){
//                                //Regnumber.setError("Already Exist");
//                                SharedPreferences sp = Signup.this.getSharedPreferences("chechUser",
//                                        MODE_PRIVATE);
//                                SharedPreferences.Editor ed = sp.edit();
//                                ed.putBoolean("statusCheck", false);
//                                ed.commit();
//                                //checkuserNumber =false;
//                            }else{
//                                SharedPreferences sp = Signup.this.getSharedPreferences("chechUser",
//                                        MODE_PRIVATE);
//                                SharedPreferences.Editor ed = sp.edit();
//                                ed.putBoolean("statusCheck", true);
//                                ed.commit();
//                                //checkuserNumber =true;
//                            }
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//
//                    secondQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            if(snapshot.exists()) {
//                                //Regusername.setError("Already Exist");
//                                SharedPreferences sp = Signup.this.getSharedPreferences("chechUser",
//                                        MODE_PRIVATE);
//                                SharedPreferences.Editor ed = sp.edit();
//                                ed.putBoolean("statusCheck", false);
//                                ed.commit();
//                                //checkuserNumber = false;
//                            }else{
//                                SharedPreferences sp = Signup.this.getSharedPreferences("chechUser",
//                                        MODE_PRIVATE);
//                                SharedPreferences.Editor ed = sp.edit();
//                                ed.putBoolean("statusCheck", true);
//                                ed.commit();
//                                //checkuserNumber =true;
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//
//                    });
//
//
//
//                }
//
//
//
//
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {}
//        };
//        reference.addChildEventListener(childEventListener);

//        Query checkUser = reference.orderByChild("username").equalTo(username);
//        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    Regusername.setError("Already Exist");
//
//                } else if (refToYourUserid.equals(number)) {
//                    Regnumber.setError("Already Exist");
//
//                }else{
//                    Toast.makeText(Signup.this, "Not Exist", Toast.LENGTH_SHORT).show();
//                }

//                    if(!checkuserNumber && !checkuserName){
//                        Regnumber.setError("Already Exist");
//                        Regusername.setError("Already Exist");
//                    }else if(!checkuserNumber && checkuserName){
//                        Regnumber.setError("Already Exist");
//                    }else if(checkuserNumber && !checkuserName){
//                        Regusername.setError("Already Exist");
//                    }else{
//                        Toast.makeText(Signup.this, "Not Exist", Toast.LENGTH_SHORT).show();
//                    }

//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

//          else {
//                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getApplicationContext(), Verify_OTP.class);
//                intent.putExtra("fullname", name);
//                intent.putExtra("username", username);
//                intent.putExtra("pass", pass);
//                intent.putExtra("phoneNo", number);
//                startActivity(intent);


            //}

            // }
        }
    }
}