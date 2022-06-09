package com.san.ripenessidentification.FragmentMain.Scanfragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.san.ripenessidentification.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class FullscreenImage extends Fragment {

    ImageView imageDisplay;
    String purl1, dateString, Unripe, Early, Partial, Full, Defect;
    TextView datename, result;
    DatabaseReference reference;
    public FullscreenImage() {
        // Required empty public constructor
    }
    public FullscreenImage(String purl, String dateName, String unripe, String earlyripe, String partially, String fully, String defect) {
        this.purl1 = purl;
        this.dateString = dateName;
        this.Unripe = unripe;
        this.Early = earlyripe;
        this.Partial = partially;
        this.Full = fully;
        this.Defect = defect;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fullscreen_image, container, false);
        imageDisplay = v.findViewById(R.id.imageRetrieve);
        datename = v.findViewById(R.id.datename);
        result = v.findViewById(R.id.showResult);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("logindata", Context.MODE_PRIVATE);
        String uid = sharedPreferences.getString("sid", String.valueOf(Context.MODE_PRIVATE));
        reference = FirebaseDatabase.getInstance().getReference("ScanHistory").child(uid);

        Glide.with(getContext()).load(purl1).into(imageDisplay);
        datename.setText(dateString);
        result.setText(Unripe+"\n"+Early+"\n"+Partial+"\n"+Full+"\n"+Defect);
        return  v;
    }


}