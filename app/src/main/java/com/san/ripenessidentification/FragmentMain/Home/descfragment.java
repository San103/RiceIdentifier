package com.san.ripenessidentification.FragmentMain.Home;

import android.media.Image;
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
import com.san.ripenessidentification.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link descfragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class descfragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String name1,desc1,purl1,fertilizerdesc,sunlightdesc,wateringdesc;
    FloatingActionButton floatingActionButton;

    public descfragment() {
        // Required empty public constructor
    }
    public descfragment(String name,String desc,String purl,String ferti,String sunlight,String water) {
        this.name1=name;
        this.desc1=desc;
        this.purl1=purl;
        this.fertilizerdesc=ferti;
        this.sunlightdesc=sunlight;
        this.wateringdesc=water;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment descfragment.
     */
    // TODO: Rename and change types and number of parameters
    public static descfragment newInstance(String param1, String param2) {
        descfragment fragment = new descfragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_descfragment, container, false);
        ImageView imageholder = v.findViewById(R.id.image_holder);
        TextView titleholder = v.findViewById(R.id.name);
        TextView descholder = v.findViewById(R.id.description);
        TextView fertilizerholder = v.findViewById(R.id.fertilizer_desc);
        TextView sunligthholder = v.findViewById(R.id.sunlight_desc);
        TextView wateringholder = v.findViewById(R.id.watering_desc);
        FloatingActionButton bckFloat = v.findViewById(R.id.back_floating);

        titleholder.setText(name1);
        descholder.setText(desc1);
        fertilizerholder.setText(fertilizerdesc);
        sunligthholder.setText(sunlightdesc);
        wateringholder.setText(wateringdesc);
        Glide.with(getContext()).load(purl1).into(imageholder);

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