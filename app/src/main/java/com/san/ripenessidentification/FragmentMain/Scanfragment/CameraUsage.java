package com.san.ripenessidentification.FragmentMain.Scanfragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.san.ripenessidentification.Admin.HelperClasses.ScanHistory;
import com.san.ripenessidentification.Admin.HelperClasses.SnapHistory;
import com.san.ripenessidentification.MainActivity;
import com.san.ripenessidentification.R;
import com.san.ripenessidentification.ml.Classesmaturity;
import com.san.ripenessidentification.ml.Maturitylives;
import com.san.ripenessidentification.ml.Ripelive;
import com.san.ripenessidentification.ml.RipenessModel;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;


public class CameraUsage extends Fragment {
    int imageSize = 224;
    ImageView imageView;
    TextView ConfResult;
    Button display;
    Uri imgscan;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("ScanHistory");
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    List<String> ListHistory;

    public CameraUsage() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_camera_usage, container, false);

        imageView = v.findViewById(R.id.imageDisplay);
        display = v.findViewById(R.id.displaybutton);
        ConfResult = v.findViewById(R.id.confidenceResult);
        ListHistory = new ArrayList<>();
        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch camera if we have permission
                if (ActivityCompat.checkSelfPermission(getContext(),android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                } else {
                    //Request camera permission if we don't have it.
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });
        return v;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {

            Bitmap image = (Bitmap) data.getExtras().get("data");
            int dimension = Math.min(image.getWidth(), image.getHeight());
            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
            imageView.setImageBitmap(image);
            image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
            classifyImage(image);
        }
    }

    private void classifyImage(Bitmap image) {
        try {
            Maturitylives model = Maturitylives.newInstance(getActivity().getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            int pixel = 0;
            for (int i = 0; i < imageSize; i++) {
                for (int j = 0; j < imageSize; j++) {
                    int val = intValues[pixel++];
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            String unripe = "", earlyripe = "", partiallyripe = "", fullyripe = "", defective = "";
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("logindata", Context.MODE_PRIVATE);
            String uid = sharedPreferences.getString("sid", String.valueOf(Context.MODE_PRIVATE));
            Maturitylives.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            String[] classes = {"Unripe", "Early Ripe","Partially Ripe", "Fully Ripe", "Defective"};
            String s = "";
            ListHistory.clear();
            for (int i = 0; i < classes.length; i++) {
                s += String.format("%s: %.1f%%\n", classes[i], confidences[i] * 100);
                ListHistory.add(String.format("%s: %.1f%%\n\n", classes[i], confidences[i] * 100));
            }
            ConfResult.setText(s);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), image, "Title", null);
            Uri imgscan = Uri.parse(path);
            unripe = ListHistory.get(0).toString();
            earlyripe = ListHistory.get(1).toString();
            partiallyripe = ListHistory.get(2).toString();
            fullyripe = ListHistory.get(3).toString();
            defective = ListHistory.get(4).toString();

            String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
            uploadToFirebase(uid, imgscan, mydate, unripe, earlyripe, partiallyripe, fullyripe, defective);
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }
    private void uploadToFirebase(String userid, Uri uri, String currentDate, String unrip, String earlyrip, String partially, String fully, String defect) {
        if (imgscan != null) {
            final StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
            fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String modelId = root.push().getKey();

                            ScanHistory scanHistory = new ScanHistory(userid, uri.toString(), currentDate, unrip, earlyrip, partially, fully, defect);
                            root.child(userid).child(modelId).setValue(scanHistory);
                            Toast.makeText(getActivity(), "Saved to History!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } else {
            Toast.makeText(getActivity(), "Image Attached", Toast.LENGTH_SHORT).show();
        }
    }
    private String getFileExtension(Uri mUri) {

        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }

}