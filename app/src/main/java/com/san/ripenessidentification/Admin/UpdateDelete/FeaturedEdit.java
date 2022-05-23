package com.san.ripenessidentification.Admin.UpdateDelete;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.san.ripenessidentification.Admin.FeaturedAdmin;
import com.san.ripenessidentification.FragmentMain.Home.MostViewedDescription;
import com.san.ripenessidentification.FragmentMain.Home.descfragment;
import com.san.ripenessidentification.HelperClasses.HomeAdapter.FeaturedAdapter;
import com.san.ripenessidentification.HelperClasses.HomeAdapter.FeaturedHelperClass;
import com.san.ripenessidentification.HelperClasses.HomeAdapter.MostViewedAdapter;
import com.san.ripenessidentification.HelperClasses.HomeAdapter.MostViewedHelperClass;
import com.san.ripenessidentification.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FeaturedEdit extends Fragment {

    EditText searchbar;
    TextView textdesc, seemore, addressusername;
    ImageView sort, addfeature;
    DatabaseReference databaseReference;
    //For Featured Recycler
    FirebaseRecyclerOptions<FeaturedHelperClass> options;
    FirebaseRecyclerAdapter<FeaturedHelperClass, FeaturedAdapter.myviewholder> adapter;
    ArrayList<FeaturedHelperClass> arrayList;

    RecyclerView MostviewedRecyclerAdmin,FeaturedRecyclerAdmin;
    public FeaturedEdit() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_featured_edit, container, false);
        addressusername = v.findViewById(R.id.addressusername);
        textdesc = v.findViewById(R.id.featured_desc);
        seemore = v.findViewById(R.id.featured_seemore);
        searchbar = v.findViewById(R.id.searchedit);
        sort = v.findViewById(R.id.sort_filter);
        addfeature = v.findViewById(R.id.add_featured);
        MostviewedRecyclerAdmin = v.findViewById(R.id.most_viewed_recycler_admin);
        FeaturedRecyclerAdmin = v.findViewById(R.id.featured_recycler_admin);


        addfeature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), FeaturedAdmin.class);
                    startActivity(intent);
                    getActivity().finish();
            }
        });
        //Search and Sort
        arrayList = new ArrayList<>();
        searchbar.addTextChangedListener(new TextWatcher() {
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
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSort();
            }
        });

        //For Featured and MostViewed
        FeaturedViewed();
        MostViewed();
        return v;
    }
    private void FeaturedViewed() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Plant");
        options = new FirebaseRecyclerOptions.Builder<FeaturedHelperClass>()
                .setQuery(databaseReference, FeaturedHelperClass.class).build();
        adapter = new FirebaseRecyclerAdapter<FeaturedHelperClass, FeaturedAdapter.myviewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FeaturedAdapter.myviewholder myviewholder, int i, @NonNull FeaturedHelperClass featuredHelperClass) {
                myviewholder.title.setText(featuredHelperClass.getPlantname());
                myviewholder.desc.setText(featuredHelperClass.getDescription());

                Glide.with(myviewholder.img.getContext()).load(featuredHelperClass.getImageUrl()).into(myviewholder.img);
                myviewholder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentAdmin, new descfragment(featuredHelperClass.getPlantname(), featuredHelperClass.getDescription(), featuredHelperClass.getImageUrl(), featuredHelperClass.getFertilizer(), featuredHelperClass.getSunlight(), featuredHelperClass.getWatering())).addToBackStack(null).commit();
                    }
                });
            }

            @NonNull
            @Override
            public FeaturedAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.featured_design, parent, false);
                return new FeaturedAdapter.myviewholder(view);
            }
        };
        FeaturedRecyclerAdmin.setHasFixedSize(true);
        FeaturedRecyclerAdmin.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        adapter.startListening();
        FeaturedRecyclerAdmin.setAdapter(adapter);

    }

    private void MostViewed() {
        final FirebaseRecyclerOptions<MostViewedHelperClass> options;
        final FirebaseRecyclerAdapter<MostViewedHelperClass, MostViewedAdapter.myviewholder> adapter;
        ArrayList<MostViewedHelperClass> arrayList;
        final DatabaseReference databaseReference;

        databaseReference = FirebaseDatabase.getInstance().getReference().child("MostViewed");
        options = new FirebaseRecyclerOptions.Builder<MostViewedHelperClass>()
                .setQuery(databaseReference, MostViewedHelperClass.class).build();
        adapter = new FirebaseRecyclerAdapter<MostViewedHelperClass, MostViewedAdapter.myviewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MostViewedAdapter.myviewholder myviewholder, int i, @NonNull MostViewedHelperClass mostViewedHelperClass) {
                myviewholder.title.setText(mostViewedHelperClass.getName());
                myviewholder.desc.setText(mostViewedHelperClass.getDescription());
                Glide.with(myviewholder.img.getContext()).load(mostViewedHelperClass.getImgUrl()).into(myviewholder.img);
                myviewholder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentAdmin, new MostViewedDescription(mostViewedHelperClass.getName(), mostViewedHelperClass.getDescription(), mostViewedHelperClass.getImgUrl())).addToBackStack(null).commit();
                    }
                });
            }


            @NonNull
            @Override
            public MostViewedAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.most_viewed, parent, false);
                return new MostViewedAdapter.myviewholder(view);
            }
        };
        MostviewedRecyclerAdmin.setHasFixedSize(true);
        MostviewedRecyclerAdmin.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter.startListening();
        MostviewedRecyclerAdmin.setAdapter(adapter);

    }


    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
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
    private void getSort(){
        Query query = databaseReference.orderByChild("plantname");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    arrayList.clear();
                    for (DataSnapshot dss : snapshot.getChildren()) {
                        final FeaturedHelperClass category = dss.getValue(FeaturedHelperClass.class);
                        arrayList.add(category);
                    }
                    Collections.sort(arrayList, new Comparator<FeaturedHelperClass>() {
                        @Override
                        public int compare(FeaturedHelperClass featuredHelperClass, FeaturedHelperClass t1) {
                            return featuredHelperClass.getPlantname().compareToIgnoreCase(t1.getPlantname());
                        }
                    });
                    FeaturedAdapter myadapter = new FeaturedAdapter(getActivity().getApplicationContext(), arrayList);
                    FeaturedRecyclerAdmin.setAdapter(myadapter);
                    myadapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void search(String s) {
        Query query = databaseReference.orderByChild("plantname")
                .startAt(s)
                .endAt(s + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    arrayList.clear();
                    for (DataSnapshot dss : snapshot.getChildren()) {
                        final FeaturedHelperClass category = dss.getValue(FeaturedHelperClass.class);
                        arrayList.add(category);
                    }

                    FeaturedAdapter myadapter = new FeaturedAdapter(getActivity().getApplicationContext(), arrayList);
                    FeaturedRecyclerAdmin.setAdapter(myadapter);
                    myadapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}