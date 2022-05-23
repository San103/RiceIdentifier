package com.san.ripenessidentification.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.san.ripenessidentification.Admin.HelperClasses.DiagnosePlantInfo;
import com.san.ripenessidentification.Admin.HelperClasses.MostViewedClass;
import com.san.ripenessidentification.R;

public class MostViewedAdmin extends AppCompatActivity {

    ImageView img;
    TextInputLayout mvname,description;
    Button btn;
    ProgressBar pb;
    MostViewedClass mostViewedClass;

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("MostViewed");
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    private Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_most_viewed_admin);
        img = findViewById(R.id.mvimageView);
        mvname = findViewById(R.id.mvname);
        description = findViewById(R.id.mvdes);
        btn = findViewById(R.id.mvupload_btn);
        pb = findViewById(R.id.mvprogressBar);

        pb.setVisibility(View.INVISIBLE);


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent , 2);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mvname.getEditText().getText().toString();
                String desc = description.getEditText().getText().toString();

                if (imageUri != null){


                    uploadToFirebase(imageUri,name, desc);
                    mvname.getEditText().setText("");
                    description.getEditText().setText("");

                }else{
                    Toast.makeText(MostViewedAdmin.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==2 && resultCode == RESULT_OK && data != null){

            imageUri = data.getData();
            img.setImageURI(imageUri);

        }
    }
    private void uploadToFirebase(Uri uri, String name, String desc){

        final StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        mostViewedClass = new MostViewedClass(name,desc,uri.toString());
                        String modelId = root.push().getKey();
//                        plantInfo.setImageUrl(model);

                        root.child(modelId).setValue(mostViewedClass);
                        pb.setVisibility(View.INVISIBLE);
                        Toast.makeText(MostViewedAdmin.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        img.setImageResource(R.drawable.img2);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                pb.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pb.setVisibility(View.INVISIBLE);
                Toast.makeText(MostViewedAdmin.this, "Uploading Failed !!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String getFileExtension(Uri mUri){

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }
}