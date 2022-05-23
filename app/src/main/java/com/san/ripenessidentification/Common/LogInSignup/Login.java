package com.san.ripenessidentification.Common.LogInSignup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.san.ripenessidentification.Admin.AdminDashboard;
import com.san.ripenessidentification.Common.OnBoarding;
import com.san.ripenessidentification.Databases.UserHelperClass;
import com.san.ripenessidentification.MainActivity;
import com.san.ripenessidentification.R;
import com.san.ripenessidentification.User.UserDashboard;
import com.san.ripenessidentification.databinding.ActivityMainBinding;


public class Login extends AppCompatActivity {

    ActivityMainBinding binding;
    TextInputLayout Username;
    TextInputLayout Password;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ImageView googleBtn;
    String st;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_login);

        Username = findViewById(R.id.login_username);
        Password = findViewById(R.id.login_pass);
        googleBtn = findViewById(R.id.googlebtn);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this, gso);
//
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);


        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SigninWithGoogle();
            }
        });

    }

    void SigninWithGoogle() {
        Intent signInIntent  = gsc.getSignInIntent();
        startActivityForResult(signInIntent,1100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1100){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
                String name = acct.getDisplayName();
                String username = acct.getGivenName();
                String uid = acct.getId();
                Uri image = acct.getPhotoUrl();
                String toimg = image.toString();
                FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
                DatabaseReference reference = rootNode.getReference("Users");
                SharedPreferences myPrefs = this.getSharedPreferences("logindata",
                        MODE_PRIVATE);
                SharedPreferences.Editor editor = myPrefs.edit();
                editor.putString("fullname", name);
                editor.putString("number", uid);
                editor.apply();

                Query checkUser = reference.orderByChild("number").equalTo(uid);
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot childDataSnapshot : snapshot.getChildren()) {
                                childDataSnapshot.getKey();
                                Object status = childDataSnapshot.child("subscription").getValue();
                                st = status.toString();
                                if(st.equals("Premium")){

                                }
                            }
                        }else{
                            UserHelperClass addNewUser = new UserHelperClass(name, username, uid, "", "Standard", "User", toimg, 0);
                            reference.child(uid).setValue(addNewUser);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




                secondAct();
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Something Went Wrong!", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    void secondAct(){
        finish();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private Boolean validateusername() {
        String val = Username.getEditText().getText().toString();
        if (val.isEmpty()) {
            Username.setError("Field cannot be empty");
            return false;
        } else {
            Username.setError(null);
            Username.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatepassword() {
        String val = Password.getEditText().getText().toString();
        if (val.isEmpty()) {
            Password.setError("Field cannot be empty");
            return false;
        } else {
            Password.setError(null);
            Password.setErrorEnabled(false);
            return true;
        }
    }

    public void dashboard(View view) {
        if (!validateusername() || !validatepassword()) {
            return;
        } else {
            isUser();
        }
    }
    public void savedata(String username,String password,String fname, String number, String ImageLink){
        SharedPreferences sharedPreferences=getSharedPreferences("logindata",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("logincounter",true);
        editor.putString("username",username);
        editor.putString("fullname",fname);
        editor.putString("number",number);
        editor.putString("password",password);
        editor.putString("imgUrl",ImageLink);
        editor.apply();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        setLoginState(false);
        intent.putExtra("fullname", fname);
        intent.putExtra("username", username);
        intent.putExtra("number", number);
        intent.putExtra("password", password);
        editor.putString("imgUrl",ImageLink);
        startActivity(intent);
    }

    private void isUser() {
        final String _username = Username.getEditText().getText().toString().trim();
        final String _password = Password.getEditText().getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        Query checkUser = reference.orderByChild("username").equalTo(_username);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    Username.setError(null);
                    Username.setEnabled(false);


                    for (DataSnapshot childDataSnapshot : snapshot.getChildren()) {
                        childDataSnapshot.getKey();
                        Object pm = childDataSnapshot.child("password").getValue();



                        if (pm.equals(_password)) {
                            Username.setError(null);
                            Username.setEnabled(false);
                            Object namefromdb = childDataSnapshot.child("fullname").getValue();
                            Object phonefromdb = childDataSnapshot.child("number").getValue();
                            Object passfromdb = childDataSnapshot.child("password").getValue();
                            Object type = childDataSnapshot.child("type").getValue();
                            Object Imagelink = childDataSnapshot.child("imgUrl").getValue();
                            if(type.equals("Admin")){
                                Intent intent = new Intent(getApplicationContext(), AdminDashboard.class);
                                startActivity(intent);
                                finish();
                            }else{
                                savedata(_username,_password,namefromdb.toString(),phonefromdb.toString(), Imagelink.toString());
                            }

                        } else {
                            Password.setError("Wrong Password");
                            Password.requestFocus();
                        }
                    }
                } else {
                    Username.setError("No record");
                    Username.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void back_to_onBoard(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void signup(View view) {
        Intent intent = new Intent(getApplicationContext(), Signup.class);
        startActivity(intent);
        finish();
    }

    public void toForgetPassword(View view) {
        Intent intent = new Intent(getApplicationContext(), forgot_password.class);
        startActivity(intent);
        finish();
    }
    private void setLoginState(boolean status) {
        SharedPreferences sp = getSharedPreferences("logindata2",
                MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("setLoggingOut", status);
        ed.commit();
    }
}