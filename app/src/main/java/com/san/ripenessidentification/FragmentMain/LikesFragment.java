package com.san.ripenessidentification.FragmentMain;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.media.ThumbnailUtils;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.san.ripenessidentification.FragmentMain.Home.descfragment;
import com.san.ripenessidentification.FragmentMain.Home.diagnosedescfragment;
import com.san.ripenessidentification.GoogleMaps.NearbyPlantPlaces;
import com.san.ripenessidentification.HelperClasses.HomeAdapter.DiagnoseAdapter;
import com.san.ripenessidentification.HelperClasses.HomeAdapter.DiagnoseHelperClass;
import com.san.ripenessidentification.HelperClasses.HomeAdapter.FeaturedAdapter;
import com.san.ripenessidentification.HelperClasses.HomeAdapter.FeaturedHelperClass;
import com.san.ripenessidentification.MainActivity;
import com.san.ripenessidentification.R;


import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class LikesFragment extends Fragment {

    Button dianosebtn;
    ImageView imgBtn;
    TextView confi;
    private AdView mAdView;
    DatabaseReference databaseReference;
    FirebaseRecyclerOptions<DiagnoseHelperClass> options;
    FirebaseRecyclerAdapter<DiagnoseHelperClass, DiagnoseAdapter.myviewholder> adapter;
    ArrayList<DiagnoseHelperClass> arrayList;
    RecyclerView diagnoseRecycler;
    EditText diagsearchbar;
    private boolean FlagStatus;
    int imageSize = 224;
    int flagClick = 0;

    public LikesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_likes, container, false);
        diagnoseRecycler = v.findViewById(R.id.diagnose_recycler);
        dianosebtn = v.findViewById(R.id.autodiagnose);
        imgBtn = v.findViewById(R.id.diagnose_profile_image);
        confi = v.findViewById(R.id.confidences);
        //mAdView = v.findViewById(R.id.adViewdiagnose);
        FlagStatus = true;

        dianosebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity)getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new ScanFragment()).addToBackStack(null).commit();
            }
        });
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

        //Ads
        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

        //SearchBar
        arrayList = new ArrayList<>();
        diagsearchbar = v.findViewById(R.id.searchdiagnose);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Diagnose");
        options = new FirebaseRecyclerOptions.Builder<DiagnoseHelperClass>()
                .setQuery(databaseReference, DiagnoseHelperClass.class).build();
        adapter = new FirebaseRecyclerAdapter<DiagnoseHelperClass, DiagnoseAdapter.myviewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DiagnoseAdapter.myviewholder myviewholder, int i, @NonNull DiagnoseHelperClass diagnoseHelperClass) {
//                myviewholder.title.setText(diagnoseHelperClass.getDiagname());
//                myviewholder.desc.setText(diagnoseHelperClass.getDiagdescription());
                //Glide.with(myviewholder.img.getContext()).load(diagnoseHelperClass.getImgUrl()).into(myviewholder.img);
//                myviewholder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
//                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new diagnosedescfragment(diagnoseHelperClass.getDiagname(), diagnoseHelperClass.getDiagdescription(), diagnoseHelperClass.getImgUrl())).addToBackStack(null).commit();
//                    }
//                });
            }

            @NonNull
            @Override
            public DiagnoseAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.diagnose_design, parent, false);
                return new DiagnoseAdapter.myviewholder(view2);
            }
        };
        diagnoseRecycler.setHasFixedSize(true);
        diagnoseRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        adapter.startListening();
        diagnoseRecycler.setAdapter(adapter);
        return v;
    }

    private void getSubscription() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("logindata", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("number", String.valueOf(Context.MODE_PRIVATE));
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        Query checkUser = reference.orderByChild("number").equalTo(name);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot childDataSnapshot : snapshot.getChildren()) {
                        childDataSnapshot.getKey();
                        Object pm = childDataSnapshot.child("subscription").getValue();
                        Object number = childDataSnapshot.child("number").getValue();
                        Object clickDiagnose = childDataSnapshot.child("clicked").getValue();
                        int click = Integer.parseInt(clickDiagnose.toString());
                        final int[] clickcount = {Integer.parseInt(clickDiagnose.toString())};



                        if (pm.equals("Premium")) {
//                            mAdView.setEnabled(false);
//                            mAdView.setVisibility(View.GONE);
//
//
//                            ViewGroup parent = (ViewGroup) mAdView.getParent();
//                            if (parent != null) parent.removeView(mAdView);
//
//                            mAdView.removeAllViews();
//                            mAdView.destroy();
                            FlagStatus = false;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();

            if(databaseReference!=null){
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        arrayList = new ArrayList<>();
                        for (DataSnapshot ds : snapshot.getChildren()){
                            arrayList.add(ds.getValue(DiagnoseHelperClass.class));
                        }
                        DiagnoseAdapter myadapter = new DiagnoseAdapter(getActivity().getApplicationContext(), arrayList);
                        diagnoseRecycler.setAdapter(myadapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            diagsearchbar.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!editable.toString().isEmpty()) {
                        search(editable.toString());
                    } else {
                        search("");
                    }
                }
            });
        }
    }

    @Override
    public void onStop() {
        if (adapter != null)
            adapter.stopListening();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.startListening();
    }

    private void search(String s) {
        ArrayList<DiagnoseHelperClass> myList = new ArrayList<>();
        for(DiagnoseHelperClass object : arrayList){
            if(object.getDiagname().toLowerCase().contains(s.toLowerCase())){
                myList.add(object);

            }
        }
        DiagnoseAdapter adapterclass =new DiagnoseAdapter(getActivity().getApplicationContext(),myList);
        diagnoseRecycler.setAdapter(adapterclass);
//        Query query = databaseReference.orderByChild("diagname")
//                .startAt(s)
//                .endAt(s + "\uf8ff");
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.hasChildren()) {
//                    arrayList.clear();
//                    for (DataSnapshot dss : snapshot.getChildren()) {
//                        final DiagnoseHelperClass category = dss.getValue(DiagnoseHelperClass.class);
//                        arrayList.add(category);
//                    }
//
//                    DiagnoseAdapter myadapter = new DiagnoseAdapter(getActivity().getApplicationContext(), arrayList);
//                    diagnoseRecycler.setAdapter(myadapter);
//                    myadapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

}