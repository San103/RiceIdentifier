package com.san.ripenessidentification.FragmentMain.Home;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.san.ripenessidentification.FragmentMain.HomeFragment;
import com.san.ripenessidentification.FragmentMain.LikesFragment;
import com.san.ripenessidentification.R;


public class MostViewedDescription extends Fragment {

    private String mParam1;
    private String mParam2;
    String name,desc,purl;
    public MostViewedDescription() {
        // Required empty public constructor
    }
    public MostViewedDescription(String name, String desc, String purl){
        this.name=name;
        this.desc=desc;
        this.purl=purl;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_most_viewed_description, container, false);
        ImageView imageholder = v.findViewById(R.id.mv_image_holder);
        TextView titleholder = v.findViewById(R.id.mvtitle);
        TextView descholder = v.findViewById(R.id.mvdes);
        FloatingActionButton bckFloat = v.findViewById(R.id.mvback_floating);

        titleholder.setText(name);
        descholder.setText(desc);
        Glide.with(getContext()).load(purl).into(imageholder);

        bckFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity)getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new HomeFragment()).addToBackStack(null).commit();
            }
        });

        return v;
    }
}