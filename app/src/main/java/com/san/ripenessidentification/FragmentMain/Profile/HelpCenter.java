package com.san.ripenessidentification.FragmentMain.Profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.san.ripenessidentification.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HelpCenter#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HelpCenter extends Fragment {



    public HelpCenter() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_help_center, container, false);

        return v;
    }
}