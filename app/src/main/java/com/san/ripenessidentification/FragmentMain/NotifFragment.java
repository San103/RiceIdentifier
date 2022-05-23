package com.san.ripenessidentification.FragmentMain;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.san.ripenessidentification.Admin.HelperClasses.ScanHistory;
import com.san.ripenessidentification.Admin.HelperClasses.ScanHistoryAdapter;
import com.san.ripenessidentification.Admin.HelperClasses.SnapHistory;
import com.san.ripenessidentification.Admin.HelperClasses.SnapHistoryAdapter;
import com.san.ripenessidentification.Common.LogInSignup.Login;
import com.san.ripenessidentification.FragmentMain.Home.descfragment;
import com.san.ripenessidentification.FragmentMain.Scanfragment.FullscreenImage;
import com.san.ripenessidentification.GoogleMaps.NearbyPlantPlaces;
import com.san.ripenessidentification.HelperClasses.HomeAdapter.FeaturedAdapter;
import com.san.ripenessidentification.HelperClasses.HomeAdapter.FeaturedHelperClass;
import com.san.ripenessidentification.HelperClasses.HomeAdapter.HistoryAdapter;
import com.san.ripenessidentification.R;

import java.util.ArrayList;
import java.util.List;

public class NotifFragment extends Fragment {
    private AdView mAdView;
    private boolean FlagStatus;
    RecyclerView HistoryRecycler;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    HistoryAdapter adapter2;
    final List<String> mArraylist = new ArrayList<>();
    //For Featured Recycler
    FirebaseRecyclerOptions<ScanHistory> options;
    FirebaseRecyclerAdapter<ScanHistory, ScanHistoryAdapter.myviewholder> adapter;
    ArrayList<ScanHistory> arrayList;

    public NotifFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_notif, container, false);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("logindata", Context.MODE_PRIVATE);
        String uid = sharedPreferences.getString("sid", String.valueOf(Context.MODE_PRIVATE));
        databaseReference = FirebaseDatabase.getInstance().getReference().child("ScanHistory").child(uid);
//        storageReference = FirebaseStorage.getInstance().getReference().child(uid + ".null");
        HistoryRecycler = v.findViewById(R.id.snaphistory_recyclerview);
        HistoryViewed();
        SharedPreferences sp = this.getActivity().getSharedPreferences("logindata2", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        Boolean counter2 = sp.getBoolean("setLoggingOut", Boolean.valueOf(String.valueOf(Context.MODE_PRIVATE)));
        if (counter2) {
            editor.putString("sid", "");
            editor.commit();
        }
        return v;
    }


    private void HistoryViewed() {
        options = new FirebaseRecyclerOptions.Builder<ScanHistory>()
                .setQuery(databaseReference, ScanHistory.class).build();
        adapter = new FirebaseRecyclerAdapter<ScanHistory, ScanHistoryAdapter.myviewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ScanHistoryAdapter.myviewholder myviewholder, int i, @NonNull ScanHistory scanHistory) {
                myviewholder.title.setText(scanHistory.getDateCaptured());
                Glide.with(myviewholder.img.getContext()).load(scanHistory.getImageUrl()).into(myviewholder.img);
                myviewholder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        activity.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContainer, new FullscreenImage(
                                        scanHistory.getImageUrl(), scanHistory.getDateCaptured(),scanHistory.getUnRipe(),scanHistory.getEarlyRipe(),scanHistory.getPartiallyRipe(),scanHistory.getFullyRipe(),scanHistory.getDefectiveness())).addToBackStack(null).commit();

                    }
                });

                myviewholder.deleteHistory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setCancelable(true);
                        builder.setTitle("Are you sure you want to delete this?");
                        builder.setMessage(scanHistory.getDateCaptured());
                        builder.setPositiveButton("Confirm",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        adapter.getRef(i).removeValue();
                                    }
                                });
                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                });
            }


            @NonNull
            @Override
            public ScanHistoryAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_design, parent, false);
                return new ScanHistoryAdapter.myviewholder(view);
            }
        };
        HistoryRecycler.setHasFixedSize(true);
        HistoryRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter.startListening();
        HistoryRecycler.setAdapter(adapter);

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
                        if (pm.equals("Premium")) {
                            mAdView.setEnabled(false);
                            mAdView.setVisibility(View.GONE);


                            ViewGroup parent = (ViewGroup) mAdView.getParent();
                            if (parent != null) parent.removeView(mAdView);

                            mAdView.removeAllViews();
                            mAdView.destroy();
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
}