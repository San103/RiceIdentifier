package com.san.ripenessidentification.FragmentMain;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.san.ripenessidentification.FragmentMain.Scanfragment.CameraUsage;
import com.san.ripenessidentification.FragmentMain.Scanfragment.ScanFromLocalStorage;
import com.san.ripenessidentification.MainActivity;
import com.san.ripenessidentification.R;
import com.san.ripenessidentification.ml.RipenessModel;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class ScanFragment extends Fragment {

    TextView confidence;
    ImageView imageView;
    Button picture;
    int imageSize = 224;
    Button identifylocal;
    String user_number;
    int flagClick = 0;


    public ScanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_scan, container, false);
        imageView = v.findViewById(R.id.imagedis);
        picture = v.findViewById(R.id.identifydis);
        identifylocal = v.findViewById(R.id.identifygallery);
        Intent intent = getActivity().getIntent();
        user_number = intent.getStringExtra("number");


        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("logindata", Context.MODE_PRIVATE);
        Integer numb = sharedPreferences.getInt("clickCount", Integer.valueOf(Context.MODE_APPEND));

        final String PREFS_NAME = "MyPrefsFile";

        SharedPreferences settings = this.getActivity().getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("my_first_time", true)) {
            //the app is being launched for first time, do something
            flagClick = 0;
            settings.edit().putBoolean("my_first_time", false).commit();
        } else {
            flagClick = numb;
        }
        getSubscription();

        return v;
    }


    private void getSubscription() {
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toFragmentScanCamera();
            }
        });
        identifylocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toFragmentScan();
            }
        });
//        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("logindata", Context.MODE_PRIVATE);
//        String numb = sharedPreferences.getString("number", String.valueOf(Context.MODE_PRIVATE));
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
//        Query checkUser = reference.orderByChild("number").equalTo(numb);
//        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    for (DataSnapshot childDataSnapshot : snapshot.getChildren()) {
//                        childDataSnapshot.getKey();
//                        Object pm = childDataSnapshot.child("clicked").getValue();
//                        Object status = childDataSnapshot.child("subscription").getValue();
//                        int click = Integer.parseInt(pm.toString());
//                        final int[] clickcount = {Integer.parseInt(pm.toString())};
//                        picture.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                clickcount[0] = clickcount[0] + 1;
//                                reference.child(numb).child("clicked").setValue(clickcount[0]);
//                                if (status.toString().equals("Standard") && click >= 5) {
//                                    Intent intent = new Intent(getActivity(), MainActivity.class);
//                                    startActivity(intent);
//                                    getActivity().finish();
//                                    reference.child(numb).child("clicked").setValue(5);
//                                    Toast.makeText(getActivity(), "You Exceeded the Standard Subscription, Buy to get Unlimited", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    reference.child(numb).child("clicked").setValue(clickcount[0]);
//                                    // Launch camera if we have permission
//                                    toFragmentScanCamera();
//
//                                }
//
//
//
//
//
//                            }
//                        });
//                        identifylocal.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                clickcount[0] = clickcount[0] + 1;
//                                reference.child(numb).child("clicked").setValue(clickcount[0]);
//                                toFragmentScan();
//                                if (status.toString().equals("Standard") && click >= 5) {
//                                    Intent intent = new Intent(getActivity(), MainActivity.class);
//                                    startActivity(intent);
//                                    getActivity().finish();
//                                    reference.child(numb).child("clicked").setValue(5);
//                                    Toast.makeText(getActivity(), "You Exceeded the Standard Subscription, Buy to get Unlimited", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    reference.child(numb).child("clicked").setValue(clickcount[0]);
//                                    toFragmentScan();
//                                }
//                            }
//                        });
//                    }
//                } else {
//                    picture.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                            final int[] clickcount = {flagClick};
//                            clickcount[0] = clickcount[0] + 1;
//                            SharedPreferences myPrefs = getContext().getSharedPreferences("logindata",
//                                    MODE_PRIVATE);
//                            SharedPreferences.Editor editor = myPrefs.edit();
//                            editor.putInt("clickCount", clickcount[0]);
//                            editor.apply();
//
//                            if (clickcount[0] >= 5) {
//                                Intent intent = new Intent(getActivity(), MainActivity.class);
//                                startActivity(intent);
//                                getActivity().finish();
//                                Toast.makeText(getActivity(), "You Exceeded the Standard Subscription, Buy to get Unlimited", Toast.LENGTH_SHORT).show();
//                            } else {
//                                toFragmentScanCamera();
//                            }
//                        }
//                    });
//
//                    identifylocal.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                            final int[] clickcount = {flagClick};
//                            clickcount[0] = clickcount[0] + 1;
//                            SharedPreferences myPrefs = getContext().getSharedPreferences("logindata",
//                                    MODE_PRIVATE);
//                            SharedPreferences.Editor editor = myPrefs.edit();
//                            editor.putInt("clickCount", clickcount[0]);
//                            editor.apply();
//
//                            if (clickcount[0] >= 5) {
//                                Intent intent = new Intent(getActivity(), MainActivity.class);
//                                startActivity(intent);
//                                getActivity().finish();
//                                Toast.makeText(getActivity(), "You Exceeded the Standard Subscription, Buy to get Unlimited", Toast.LENGTH_SHORT).show();
//                            } else {
//                                ScanFromLocalStorage newFragment = new ScanFromLocalStorage();
//                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                                transaction.replace(R.id.fragmentContainer, newFragment);
//                                transaction.addToBackStack(null);
//                                transaction.commit();
////                                Intent intent = new Intent(getActivity(), FromLocalStorage.class);
////                                startActivity(intent);
////                                getActivity().finish();
//                            }
//
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }
    private void toFragmentScan(){
        ScanFromLocalStorage newFragment = new ScanFromLocalStorage();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void toFragmentScanCamera(){
        CameraUsage newFragment = new CameraUsage();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
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
            RipenessModel model = RipenessModel.newInstance(getActivity().getApplicationContext());

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
            RipenessModel.Outputs outputs = model.process(inputFeature0);
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
            String[] classes = {"Unripe", "Early Ripe","Partially Ripe", "Fully Ripe", "Defectiveness"};
            String s = "";
            for (int i = 0; i < classes.length; i++) {
                s += String.format("%s: %.1f%%\n", classes[i], confidences[i] * 100);
            }
            confidence.setText(s);
            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }
}