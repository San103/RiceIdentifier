
package com.san.ripenessidentification.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.san.ripenessidentification.R;
import com.san.ripenessidentification.Admin.HelperClasses.Model;
import com.san.ripenessidentification.Admin.HelperClasses.PlantInfo;


public class FeaturedAdmin extends AppCompatActivity {

    //widgets

    private Button uploadBtn;
    private ImageView imageView;
    private ProgressBar progressBar;
    TextInputLayout Plantname;
    TextInputLayout Description;
    TextInputLayout Fertilizer;
    TextInputLayout Sunlight;
    TextInputLayout Watering;
    PlantInfo plantInfo;

    //vars
    DatabaseReference databaseReference;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Plant");
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_featured_admin);


        uploadBtn = findViewById(R.id.upload_btn);
        progressBar = findViewById(R.id.progressBar);
        imageView = findViewById(R.id.imageView);

        Plantname = findViewById(R.id.plantname);
        Description = findViewById(R.id.desc);
        Fertilizer = findViewById(R.id.fertilizer);
        Sunlight = findViewById(R.id.sunlight);
        Watering = findViewById(R.id.watering);


        progressBar.setVisibility(View.INVISIBLE);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = Plantname.getEditText().getText().toString();
                String desc = Description.getEditText().getText().toString();
                String ferti = Fertilizer.getEditText().getText().toString();
                String sun = Sunlight.getEditText().getText().toString();
                String water = Watering.getEditText().getText().toString();
                if (imageUri != null) {


                    uploadToFirebase(imageUri, name, desc, ferti, sun, water);
                    Plantname.getEditText().setText("");
                    Description.getEditText().setText("");
                    Fertilizer.getEditText().setText("");
                    Sunlight.getEditText().setText("");
                    Watering.getEditText().setText("");
                } else {
                    Toast.makeText(FeaturedAdmin.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {

            imageUri = data.getData();
            imageView.setImageURI(imageUri);

        }
    }

    private void uploadToFirebase(Uri uri, String name, String desc, String ferti, String suns, String water) {

        final StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        plantInfo = new PlantInfo(name, desc, ferti, suns, water, uri.toString());
                        String modelId = root.push().getKey();
//                        plantInfo.setImageUrl(model);

                        root.child(modelId).setValue(plantInfo);
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(FeaturedAdmin.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        imageView.setImageResource(R.drawable.img2);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(FeaturedAdmin.this, "Uploading Failed !!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri mUri) {

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }

}