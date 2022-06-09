package com.san.ripenessidentification.FragmentMain.Scanfragment;

import android.Manifest;
import android.app.Activity;
import android.app.DirectAction;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.san.ripenessidentification.Admin.HelperClasses.ScanHistory;
import com.san.ripenessidentification.Admin.HelperClasses.SnapHistory;
import com.san.ripenessidentification.FragmentMain.ScanFragment;
import com.san.ripenessidentification.MainActivity;
import com.san.ripenessidentification.R;
import com.san.ripenessidentification.ml.RipenessModel;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.TensorOperator;
import org.tensorflow.lite.support.common.TensorProcessor;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp;
import org.tensorflow.lite.support.label.TensorLabel;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.BlockingDeque;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


public class ScanFromLocalStorage extends Fragment {

    protected Interpreter tflite;
    private MappedByteBuffer tfliteModel;
    private TensorImage inputImageBuffer;
    private int imageSizeX;
    private int imageSizeY;
    private TensorBuffer outputProbabilityBuffer;
    private TensorProcessor probabilityProcessor;
    private static final float IMAGE_MEAN = 0.0f;
    private static final float IMAGE_STD = 1.0f;
    private static final float PROBABILITY_MEAN = 0.0f;
    private static final float PROBABILITY_STD = 255.0f;
    private Bitmap bitmap;
    private List<String> labels;
    private HorizontalBarChart mBarChart;
    ImageView imageView;
    Uri imageuri, urimg;
    SnapHistory snapHistory;
    int imageSize = 224;
    Button buclassify;
    TextView savebtn, displayMessage, plantCondition;
    private static final int REQUEST_EXTERNAL_STORAGe = 1;
    private static String[] permissionstorage = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("ScanHistory");
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    TextView prediction;
    List<String> ListHistory;

    public ScanFromLocalStorage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_scan_from_local_storage, container, false);
        imageView = (ImageView) v.findViewById(R.id.image);
        buclassify = (Button) v.findViewById(R.id.classify);
        prediction = (TextView) v.findViewById(R.id.predictions);
        savebtn = (TextView) v.findViewById(R.id.saveclassify);
        displayMessage = (TextView) v.findViewById(R.id.displayMessagestatus);
        plantCondition = (TextView) v.findViewById(R.id.plantcondition);
        mBarChart = v.findViewById(R.id.chart);
        ListHistory = new ArrayList<>();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 12);
            }
        });

        try {
            tflite = new Interpreter(loadmodelfile(getActivity()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        buclassify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int imageTensorIndex = 0;
                int[] imageShape = tflite.getInputTensor(imageTensorIndex).shape(); // {1, height, width, 3}
                imageSizeY = imageShape[1];
                imageSizeX = imageShape[2];
                DataType imageDataType = tflite.getInputTensor(imageTensorIndex).dataType();

                int probabilityTensorIndex = 0;
                int[] probabilityShape =
                        tflite.getOutputTensor(probabilityTensorIndex).shape(); // {1, NUM_CLASSES}
                DataType probabilityDataType = tflite.getOutputTensor(probabilityTensorIndex).dataType();

                inputImageBuffer = new TensorImage(imageDataType);
                outputProbabilityBuffer = TensorBuffer.createFixedSize(probabilityShape, probabilityDataType);
                probabilityProcessor = new TensorProcessor.Builder().add(getPostprocessNormalizeOp()).build();

                inputImageBuffer = loadImage(bitmap);

                tflite.run(inputImageBuffer.getBuffer(), outputProbabilityBuffer.getBuffer().rewind());
                showresult();


            }
        });
        return v;
    }

    public Bitmap screenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private void uploadToFirebase(String userid, Uri uri, String currentDate, String unrip, String earlyrip, String partially, String fully, String defect) {
        if (imageuri != null) {
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
            Toast.makeText(getActivity(), "No Image Attached", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri mUri) {

        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }

    private TensorImage loadImage(final Bitmap bitmap) {
        // Loads bitmap into a TensorImage.
        inputImageBuffer.load(bitmap);

        // Creates processor for the TensorImage.
        int cropSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
        // TODO(b/143564309): Fuse ops inside ImageProcessor.
        ImageProcessor imageProcessor =
                new ImageProcessor.Builder()
                        .add(new ResizeWithCropOrPadOp(cropSize, cropSize))
                        .add(new ResizeOp(imageSizeX, imageSizeY, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
                        .add(getPreprocessNormalizeOp())
                        .build();
        return imageProcessor.process(inputImageBuffer);
    }

    private MappedByteBuffer loadmodelfile(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd("ripenessmodel.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startoffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startoffset, declaredLength);
    }

    private TensorOperator getPreprocessNormalizeOp() {
        return new NormalizeOp(IMAGE_MEAN, IMAGE_STD);
    }

    private TensorOperator getPostprocessNormalizeOp() {
        return new NormalizeOp(PROBABILITY_MEAN, PROBABILITY_STD);
    }

    public static void barchart(BarChart barChart, ArrayList<BarEntry> arrayList, final ArrayList<String> xAxisValues) {
        barChart.setDrawBarShadow(false);
        barChart.setFitBars(true);
        barChart.setDrawValueAboveBar(true);
        barChart.setMaxVisibleValueCount(25);
        barChart.setPinchZoom(true);

        barChart.setDrawGridBackground(true);
        BarDataSet barDataSet = new BarDataSet(arrayList, "Class");
        barDataSet.setColors(new int[]{Color.parseColor("#03A9F4"), Color.parseColor("#FF9800"),
                Color.parseColor("#76FF03"), Color.parseColor("#E91E63"), Color.parseColor("#2962FF")});
//        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.9f);
        barData.setValueTextSize(0f);

        barChart.setBackgroundColor(Color.TRANSPARENT); //set whatever color you prefer
        barChart.setDrawGridBackground(false);
        barChart.animateY(2000);

        //Legend l = barChart.getLegend(); // Customize the ledgends
        //l.setTextSize(10f);
        //l.setFormSize(10f);
//To set components of x axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setTextSize(13f);
        xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisValues));
        xAxis.setDrawGridLines(false);
        barChart.setData(barData);

    }


    private void showresult() {
        float defect = 0;
        String unripe = "", earlyripe = "", partiallyripe = "", fullyripe = "", defective = "";
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("logindata", Context.MODE_PRIVATE);
        String uid = sharedPreferences.getString("sid", String.valueOf(Context.MODE_PRIVATE));
        try {
            labels = FileUtil.loadLabels(getActivity(), "ripenesslabels.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, Float> labeledProbability =
                new TensorLabel(labels, probabilityProcessor.process(outputProbabilityBuffer))
                        .getMapWithFloatValue();
        float maxValueInMap = (Collections.max(labeledProbability.values()));

        for (Map.Entry<String, Float> entry : labeledProbability.entrySet()) {
            //if (entry.getValue()==maxValueInMap) {

            String[] label = labeledProbability.keySet().toArray(new String[0]);
            Float[] label_probability = labeledProbability.values().toArray(new Float[0]);
            mBarChart.getXAxis().setDrawGridLines(false);
            mBarChart.getAxisLeft().setDrawGridLines(false);
            // PREPARING THE ARRAY LIST OF BAR ENTRIES
            ArrayList<BarEntry> barEntries = new ArrayList<>();
            for (int i = 0; i < label_probability.length; i++) {
                barEntries.add(new BarEntry(i, label_probability[i] * 100));
            }
            String[] classes = {"Unripe", "Early Ripe", "Partially Ripe", "Fully Ripe", "Defective"};
            String s = "";


            ListHistory.clear();
            for (int i = 0; i < classes.length; i++) {
                s += String.format("%s:       %.1f%%\n\n", classes[i], label_probability[i] * 100);
                ListHistory.add(String.format("%s: %.1f%%\n\n", classes[i], label_probability[i] * 100));
            }

            savebtn.setText(s);

            // TO ADD THE VALUES IN X-AXIS
            ArrayList<String> xAxisName = new ArrayList<>();
            for (int i = 0; i < label.length; i++) {
                xAxisName.add(label[i]);
            }
            barchart(mBarChart, barEntries, xAxisName);
            prediction.setText("Predictions:");
        }
        unripe = ListHistory.get(0).toString();
        earlyripe = ListHistory.get(1).toString();
        partiallyripe = ListHistory.get(2).toString();
        fullyripe = ListHistory.get(3).toString();
        defective = ListHistory.get(4).toString();


        String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        if (uid.equals("0")){
            Toast.makeText(getActivity(), "History Not Saved!", Toast.LENGTH_SHORT).show();
        }else{
            uploadToFirebase(uid, imageuri, mydate, unripe, earlyripe, partiallyripe, fullyripe, defective);
        }


        String[] separated = defective.split(":");
        String clean = separated[1].trim().replace("%","");
        defect =Float.parseFloat(clean.toString());

        if (defect<=50 && defect>30){
            displayMessage.setText("Slightly in Good Condition");
            plantCondition.setText("Fair");
            plantCondition.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }else if(defect>50) {
            displayMessage.setText("Your plant is defenitely NOT in Good Condition");
            plantCondition.setText("Critical");
            plantCondition.setTextColor(getResources().getColor(R.color.colorRedDark));
        }else {
            plantCondition.setText("Healthy");
            displayMessage.setText("Your plant is in Good Condition");
            plantCondition.setTextColor(getResources().getColor(R.color.colorGreenDark));
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 12 && resultCode == RESULT_OK && data != null) {
            imageuri = data.getData();
            imageView.setImageURI(imageuri);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageuri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


}