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
import com.san.ripenessidentification.R;
import com.san.ripenessidentification.Admin.HelperClasses.DiagnosePlantInfo;
import com.san.ripenessidentification.Admin.HelperClasses.PlantInfo;


public class diagnoseactivity extends AppCompatActivity {

    private Button diaguploadBtn;
    private ImageView diagimageView;
    private ProgressBar diagprogressBar;
    TextInputLayout diagplantname;
    TextInputLayout diagdes;
    DiagnosePlantInfo diagnosePlantInfo;

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Diagnose");
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    private Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnoseactivity);
        diaguploadBtn= findViewById(R.id.diagupload_btn);
        diagimageView= findViewById(R.id.diagimageView);
        diagplantname= findViewById(R.id.diagname);
        diagdes= findViewById(R.id.diagdes);
        diagprogressBar = findViewById(R.id.diagprogressBar);

        diagprogressBar.setVisibility(View.INVISIBLE);


        diagimageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent , 2);
            }
        });

        diaguploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = diagplantname.getEditText().getText().toString();
                String desc = diagdes.getEditText().getText().toString();
                if (imageUri != null){
                    uploadToFirebase(imageUri,name, desc);
                    diagplantname.getEditText().setText("");
                    diagdes.getEditText().setText("");

                }else{
                    Toast.makeText(diagnoseactivity.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==2 && resultCode == RESULT_OK && data != null){

            imageUri = data.getData();
            diagimageView.setImageURI(imageUri);

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

                        diagnosePlantInfo = new DiagnosePlantInfo(name,desc,uri.toString());
                        String modelId = root.push().getKey();
//                        plantInfo.setImageUrl(model);

                        root.child(modelId).setValue(diagnosePlantInfo);
                        diagprogressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(diagnoseactivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        diagimageView.setImageResource(R.drawable.img2);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                diagprogressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                diagprogressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(diagnoseactivity.this, "Uploading Failed !!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri mUri){

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }
}