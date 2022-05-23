
package com.san.ripenessidentification.FragmentMain.Profile;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.san.ripenessidentification.Common.LogInSignup.Login;
import com.san.ripenessidentification.MainActivity;
import com.san.ripenessidentification.R;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_APPEND;
import static android.content.Context.MODE_PRIVATE;

public class AccountSettings extends Fragment {


    ImageView profileimg;
    Button logout, Update;
    Object fname, phone, uname, pw, imgLink;
    EditText Fullname, Username, Phone, Oldpass, Newpass;
    TextView Fnameprofile, statusSub;
    String user_fullname, user_nickname, user_number, user_oldpass;
    DatabaseReference reference;
    private Uri imageUri;
    boolean checkuserName;
    //For Google
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    public AccountSettings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_account_settings, container, false);
        reference = FirebaseDatabase.getInstance().getReference("Users");


        logout = v.findViewById(R.id.logoutbtnni);
        Update = v.findViewById(R.id.updatebtnn);
        Fullname = v.findViewById(R.id.fullnameedit);
        Username = v.findViewById(R.id.usernameedit);
        Phone = v.findViewById(R.id.numberedit);
        Fnameprofile = v.findViewById(R.id.fnameprofile);
        Oldpass = v.findViewById(R.id.oldpassedit);
        Newpass = v.findViewById(R.id.newpass);
        profileimg = v.findViewById(R.id.profile_image);
        Phone.setEnabled(false);

        Intent intent = getActivity().getIntent();
        user_fullname = intent.getStringExtra("fullname");
        user_nickname = intent.getStringExtra("username");
        user_number = intent.getStringExtra("number");
        user_oldpass = intent.getStringExtra("password");

        Fnameprofile.setText(user_fullname);
        Fullname.setText(user_fullname);
        Username.setText(user_nickname);
        Phone.setText(user_number);
        //For Google Sign In
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(getActivity(), gso);


        profileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
                if (acct != null) {
                    gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            SharedPreferences myPrefs = getContext().getSharedPreferences("logindata",
                                    MODE_PRIVATE);
                            SharedPreferences.Editor editor = myPrefs.edit();
                            editor.clear();
                            editor.commit();
                            setLoginState(true);
                            Intent intent = new Intent(getContext(),
                                    MainActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    });
                } else {
                    SharedPreferences myPrefs = getContext().getSharedPreferences("logindata",
                            MODE_PRIVATE);
                    SharedPreferences.Editor editor = myPrefs.edit();
                    editor.clear();
                    editor.commit();
                    setLoginState(true);
                    Intent intent = new Intent(getContext(),
                            MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
        updatevalues();
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNameChangedShared();
                try {
                    if (isUsernameChanged() || isNameChanged()) {
                        Toast.makeText(getActivity(), "Data has been Updated", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {

                }


            }
        });
        return v;
    }

    private String getFileExtension(Uri mUri) {

        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {

            imageUri = data.getData();
            profileimg.setImageURI(imageUri);
            uploadImage();

        }
    }

    private void uploadImage() {
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Uploading");
        pd.show();

        if (imageUri != null) {
            final StorageReference fileRef = FirebaseStorage.getInstance().getReference().child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            fileRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            String ss = Phone.getText().toString();
                            reference.child(ss).child("imgUrl").setValue(url);
                            SharedPreferences myPrefs = getContext().getSharedPreferences("logindata",
                                    MODE_PRIVATE);
                            SharedPreferences.Editor editor = myPrefs.edit();
                            editor.putString("imgUrl", url);
                            editor.apply();
                            Picasso.with(getContext()).load(url).into(profileimg);
                            pd.dismiss();
                            Toast.makeText(getContext(), "Image Upload Success", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

    }


    private boolean isUsernameChanged() {

        if (!user_nickname.equals(Username.getText().toString())) {
            reference.child(user_number).child("username").setValue(Username.getText().toString());
            user_nickname = Username.getText().toString();
            SharedPreferences myPrefs = getContext().getSharedPreferences("logindata",
                    MODE_PRIVATE);
            SharedPreferences.Editor editor = myPrefs.edit();
            editor.putString("username", user_nickname);
            editor.apply();


            return true;
        } else {
            return false;
        }
    }


    private boolean isNameChanged() {
        if (!user_fullname.equals(Fullname.getText().toString())) {
            reference.child(user_number).child("fullname").setValue(Fullname.getText().toString());
            user_fullname = Fullname.getText().toString();
            Fnameprofile.setText(user_fullname);
            SharedPreferences myPrefs = getContext().getSharedPreferences("logindata",
                    MODE_PRIVATE);
            SharedPreferences.Editor editor = myPrefs.edit();
            editor.putString("fullname", user_fullname);
            editor.apply();
            return true;
        } else {
            return false;
        }
    }


    private void updatevalues() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("logindata", Context.MODE_PRIVATE);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        Query checkUser = reference.orderByChild("fullname").equalTo(user_fullname);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot childDataSnapshot : snapshot.getChildren()) {
                        childDataSnapshot.getKey();
                        fname = childDataSnapshot.child("fullname").getValue();
                        uname = childDataSnapshot.child("username").getValue();
                        phone = childDataSnapshot.child("number").getValue();
                        pw = childDataSnapshot.child("password").getValue();
                        imgLink = childDataSnapshot.child("imgUrl").getValue();
                        Fullname.setText(fname.toString());
                        Fnameprofile.setText(fname.toString());
                        Username.setText(uname.toString());
                        Phone.setText(phone.toString());
                        if (imgLink.toString() != "") {
                            Picasso.with(getContext()).load(imgLink.toString()).into(profileimg);
                        } else {
                            profileimg.setImageResource(R.drawable.user_profile);
                        }

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("fullname", fname.toString());
                        editor.putString("username", uname.toString());
                        editor.putString("number", phone.toString());
                        editor.putString("password", pw.toString());
                        editor.putString("imgUrl", imgLink.toString());
                        editor.apply();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void isNameChangedShared() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("logindata", Context.MODE_PRIVATE);
        Boolean counter = sharedPreferences.getBoolean("logincounter", Boolean.valueOf(String.valueOf(Context.MODE_PRIVATE)));
        String nameupdate = sharedPreferences.getString("fullname", String.valueOf(Context.MODE_APPEND));
        String userupdate = sharedPreferences.getString("username", String.valueOf(Context.MODE_APPEND));
        String pwupdate = sharedPreferences.getString("password", String.valueOf(Context.MODE_APPEND));
        String numbb = sharedPreferences.getString("number", String.valueOf(Context.MODE_APPEND));
        String imgUpdate = sharedPreferences.getString("imgUrl", String.valueOf(Context.MODE_APPEND));

        if (counter) {
            Picasso.with(getContext()).load(imgUpdate).into(profileimg);
            //Update Fullname
            if (!nameupdate.equals(Fullname.getText().toString())) {
                reference.child(numbb).child("fullname").setValue(Fullname.getText().toString());
                nameupdate = Fullname.getText().toString();
                Fnameprofile.setText(nameupdate);
                SharedPreferences myPrefs = getContext().getSharedPreferences("logindata",
                        MODE_PRIVATE);
                SharedPreferences.Editor editor = myPrefs.edit();
                editor.putString("fullname", nameupdate);
                editor.apply();
                Toast.makeText(getActivity(), "Data Udpdated Successfully", Toast.LENGTH_SHORT).show();
            }
            //Update Username
            if (!userupdate.equals(Username.getText().toString())) {
                reference.child(numbb).child("username").setValue(Username.getText().toString());
                userupdate = Username.getText().toString();
                Username.setText(userupdate);
                SharedPreferences myPrefs = getContext().getSharedPreferences("logindata",
                        MODE_PRIVATE);
                SharedPreferences.Editor editor = myPrefs.edit();
                editor.putString("username", userupdate);
                editor.apply();
                Toast.makeText(getActivity(), "Data Udpdated Successfully", Toast.LENGTH_SHORT).show();
            }
            //for Password
            if (pwupdate.equals(Oldpass.getText().toString())) {
                if (Oldpass.getText().toString().equals(Newpass.getText().toString())) {
                    Toast.makeText(getActivity(), "Same as old, Nothing's changed!", Toast.LENGTH_SHORT).show();
                    Oldpass.setText("");
                    Newpass.setText("");
                } else {
                    reference.child(numbb).child("password").setValue(Newpass.getText().toString());
                    Toast.makeText(getActivity(), "Password Updated Successfully", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("password", Newpass.getText().toString());
                    editor.apply();
                    Oldpass.setText("");
                    Newpass.setText("");
                }
            }

        }
    }

    private void setLoginState(boolean status) {
        SharedPreferences sp = getContext().getSharedPreferences("logindata2",
                MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("setLoggingOut", status);
        ed.commit();
    }

    private void checuserstatus() {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        SharedPreferences sp = this.getActivity().getSharedPreferences("logindata2", Context.MODE_PRIVATE);
        Boolean counter2 = sp.getBoolean("setLoggingOut", Boolean.valueOf(String.valueOf(Context.MODE_PRIVATE)));
        if (counter2 || Fnameprofile.equals("Log in here")) {
            Fnameprofile.setText("Log in here");
        }
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("logindata", Context.MODE_PRIVATE);
        Boolean counter = sharedPreferences.getBoolean("logincounter", Boolean.valueOf(String.valueOf(Context.MODE_PRIVATE)));
        String username = sharedPreferences.getString("username", String.valueOf(Context.MODE_PRIVATE));
        String name = sharedPreferences.getString("fullname", String.valueOf(Context.MODE_PRIVATE));
        String ImageUrl = sharedPreferences.getString("imgUrl", String.valueOf(MODE_PRIVATE));
        String number_user = sharedPreferences.getString("number", String.valueOf(Context.MODE_PRIVATE));

        if (counter) {
            Fnameprofile.setText(name);
            Fullname.setText(name);
            Username.setText(username);
            Phone.setText(number_user);
            if (ImageUrl != "") {
                Picasso.with(getContext()).load(ImageUrl).into(profileimg);
            } else {
                profileimg.setImageResource(R.drawable.user_profile);
            }


        } else if (acct != null) {
            String personName = acct.getDisplayName();
            String personUsername = acct.getGivenName();
            String personId = acct.getId();
            Uri personImage = acct.getPhotoUrl();
            String toStringImg = personImage.toString();

            Fnameprofile.setText(personName);
            Fullname.setText(personName);
            Username.setText(personUsername);
            Phone.setText(personId);
            Picasso.with(getContext()).load(toStringImg).into(profileimg);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        updatevalues();
        checuserstatus();

    }

}