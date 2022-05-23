package com.san.ripenessidentification.FragmentMain;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.san.ripenessidentification.Common.LogInSignup.Login;
import com.san.ripenessidentification.FragmentMain.Home.MostViewedDescription;
import com.san.ripenessidentification.FragmentMain.Home.descfragment;
import com.san.ripenessidentification.HelperClasses.HomeAdapter.DiagnoseAdapter;
import com.san.ripenessidentification.HelperClasses.HomeAdapter.DiagnoseHelperClass;
import com.san.ripenessidentification.HelperClasses.HomeAdapter.FeaturedAdapter;
import com.san.ripenessidentification.HelperClasses.HomeAdapter.FeaturedHelperClass;
import com.san.ripenessidentification.HelperClasses.HomeAdapter.MostViewedAdapter;
import com.san.ripenessidentification.HelperClasses.HomeAdapter.MostViewedHelperClass;
import com.san.ripenessidentification.MainActivity;
import com.san.ripenessidentification.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment {
    RecyclerView featuredRecycler;
    EditText searchbar;
    TextView textdesc, seemore, addressusername;
    ImageView sort;
    DatabaseReference databaseReference;
    private AdView mAdView, mAdView2;
    private InterstitialAd mInterstitialAd;
    //For Google
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    private boolean FlagStatus;


    //For Featured Recycler
    FirebaseRecyclerOptions<FeaturedHelperClass> options;
    FirebaseRecyclerAdapter<FeaturedHelperClass, FeaturedAdapter.myviewholder> adapter;
    ArrayList<FeaturedHelperClass> arrayList;

    //for MostViewed Recycler


    RecyclerView mostviewedRecycler;
    LinearLayout linearLayout;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Plant");
        addressusername = v.findViewById(R.id.addressusername);
        featuredRecycler = v.findViewById(R.id.featured_recycler);
        mostviewedRecycler = v.findViewById(R.id.most_viewed_recycler);
        linearLayout = v.findViewById(R.id.linearclick);
        textdesc = v.findViewById(R.id.featured_desc);
        seemore = v.findViewById(R.id.featured_seemore);
        searchbar = v.findViewById(R.id.searchedit);
        sort = v.findViewById(R.id.sort_filter);
//        mAdView = v.findViewById(R.id.adView);
//        mAdView2 = v.findViewById(R.id.adView2);


        //For Ads
        FlagStatus = true;
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        getSubscription();

        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
//        mAdView2.loadAd(adRequest);

//        InterstitialAd.load(getActivity(), "ca-app-pub-3940256099942544/1033173712", adRequest,
//                new InterstitialAdLoadCallback() {
//
//                    @Override
//                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
//                        // The mInterstitialAd reference will be null until
//                        // an ad is loaded.
//                        mInterstitialAd = interstitialAd;
//
//                        if (mInterstitialAd != null) {
//                            if (FlagStatus) {
//                                mInterstitialAd.show(getActivity());
//                            } else
//                                mInterstitialAd = null;
//                        } else {
//                            Log.d("TAG", "The interstitial ad wasn't ready yet.");
//                        }
//                    }
//
//                    @Override
//                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                        // Handle the error
//                        Log.i(TAG, loadAdError.getMessage());
//                        mInterstitialAd = null;
//                    }
//                });


        //For Google Sign In
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(getActivity(), gso);


        // For Log In session
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Login.class);
                setLoginState(true);
                startActivity(intent);
            }
        });

        //Search and Sort
        arrayList = new ArrayList<>();
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
                        SharedPreferences.Editor ed = sharedPreferences.edit();
                        ed.putString("sid", name);
                        ed.commit();
                        if (pm.equals("Premium")) {
//                            mAdView.setEnabled(false);
//                            mAdView2.setEnabled(false);
//                            mAdView.setVisibility(View.GONE);
//                            mAdView2.setVisibility(View.GONE);
//
//                            ViewGroup parent = (ViewGroup) mAdView.getParent();
//                            ViewGroup parent2 = (ViewGroup) mAdView2.getParent();
//                            if (parent != null) parent.removeView(mAdView);
//                            if (parent2 != null) parent2.removeView(mAdView2);
//
//                            mAdView.removeAllViews();
//                            mAdView2.removeAllViews();
//                            mAdView.destroy();
//                            mAdView2.destroy();
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

    private void FeaturedViewed() {
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
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new descfragment(featuredHelperClass.getPlantname(), featuredHelperClass.getDescription(), featuredHelperClass.getImageUrl(), featuredHelperClass.getFertilizer(), featuredHelperClass.getSunlight(), featuredHelperClass.getWatering())).addToBackStack(null).commit();
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
        featuredRecycler.setHasFixedSize(true);
        featuredRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        adapter.startListening();
        featuredRecycler.setAdapter(adapter);

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
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new MostViewedDescription(mostViewedHelperClass.getName(), mostViewedHelperClass.getDescription(), mostViewedHelperClass.getImgUrl())).addToBackStack(null).commit();
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
        mostviewedRecycler.setHasFixedSize(true);
        mostviewedRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter.startListening();
        mostviewedRecycler.setAdapter(adapter);

    }


    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
        checuserstatus();
        getSubscription();
        if(databaseReference!=null){
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    arrayList = new ArrayList<>();
                    for (DataSnapshot ds : snapshot.getChildren()){
                        arrayList.add(ds.getValue(FeaturedHelperClass.class));
                    }
                    FeaturedAdapter myadapter = new FeaturedAdapter(getActivity().getApplicationContext(), arrayList);
                    featuredRecycler.setAdapter(myadapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
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
    }

    private void setLoginState(boolean status) {
        SharedPreferences sp = getContext().getSharedPreferences("logindata2",
                MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("setLoggingOut", status);
        ed.commit();
    }

    private void checuserstatus() {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        SharedPreferences sp = this.getActivity().getSharedPreferences("logindata2", Context.MODE_PRIVATE);
        Boolean counter2 = sp.getBoolean("setLoggingOut", Boolean.valueOf(String.valueOf(Context.MODE_PRIVATE)));
        if (counter2 || addressusername.equals("Log in")) {
            addressusername.setText("Log in");
        }

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("logindata", Context.MODE_PRIVATE);
        Boolean counter = sharedPreferences.getBoolean("logincounter", Boolean.valueOf(String.valueOf(Context.MODE_PRIVATE)));
        String username = sharedPreferences.getString("username", String.valueOf(Context.MODE_APPEND));
        String usernameupdate = sharedPreferences.getString("usernameupdate", String.valueOf(Context.MODE_PRIVATE));
        if (counter) {

            addressusername.setText("Hi, " + username);
            linearLayout.setClickable(false);
        }else if ( acct!=null){
            String personName = acct.getGivenName();
            addressusername.setText("Hi, " + personName);
            linearLayout.setClickable(false);
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

    private void getSort() {
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
                    featuredRecycler.setAdapter(myadapter);
                    myadapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void search(String s) {
        ArrayList<FeaturedHelperClass> myList = new ArrayList<>();
        for(FeaturedHelperClass object : arrayList){
            if(object.getPlantname().toLowerCase().contains(s.toLowerCase())){
                myList.add(object);
            }
        }
        FeaturedAdapter adapterclass =new FeaturedAdapter(getActivity().getApplicationContext(),myList);
        featuredRecycler.setAdapter(adapterclass);
//        Query query = databaseReference.orderByChild("plantname")
//                .startAt(s)
//                .endAt(s + "\uf8ff");
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.hasChildren()) {
//                    arrayList.clear();
//                    for (DataSnapshot dss : snapshot.getChildren()) {
//                        final FeaturedHelperClass category = dss.getValue(FeaturedHelperClass.class);
//                        arrayList.add(category);
//                    }
//
//                    FeaturedAdapter myadapter = new FeaturedAdapter(getActivity().getApplicationContext(), arrayList);
//                    featuredRecycler.setAdapter(myadapter);
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