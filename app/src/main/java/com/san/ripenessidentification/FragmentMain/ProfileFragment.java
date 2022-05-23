package com.san.ripenessidentification.FragmentMain;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.interfaces.HttpResponseCallback;
import com.braintreepayments.api.internal.HttpClient;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.san.ripenessidentification.Common.LogInSignup.Login;
import com.san.ripenessidentification.FragmentMain.Home.descfragment;
import com.san.ripenessidentification.FragmentMain.Profile.AccountSettings;
import com.san.ripenessidentification.FragmentMain.Profile.AppInformation;
import com.san.ripenessidentification.FragmentMain.Profile.HelpCenter;
import com.san.ripenessidentification.GoogleMaps.NearbyPlantPlaces;
import com.san.ripenessidentification.HelperClasses.PaypalClientIdConfig;
import com.san.ripenessidentification.R;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import hotchemi.android.rate.AppRate;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {


    private static final int REQUEST_CODE = 123;
    TextView fname, number;
    Button premiumbtn;
    DatabaseReference reference;
    String user_number, user_name, user_image;
    ImageView diamond, profileimg;
    LinearLayout share;
    LinearLayout rateApp, plantArticles, findShop, helpCenter, appInfo;
    //For Google
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    LinearLayout accountsettings;
    final String API_GET_TOKEN = "http://10.0.2.2/braintree/main.php";
    final String API_CHECK_OUT = "http://10.0.2.2/braintree/checkout.php";

    String token, amount = "100";
    HashMap<String, String> paramsHash;

    public ProfileFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        reference = FirebaseDatabase.getInstance().getReference("Users");
        premiumbtn = v.findViewById(R.id.premiumbtn);
        fname = v.findViewById(R.id.fnameprofile);
        profileimg = v.findViewById(R.id.profile_image);
        number = v.findViewById(R.id.numberprofile);
        diamond = v.findViewById(R.id.diamond);
        share = v.findViewById(R.id.tellFriends);
        accountsettings = v.findViewById(R.id.accounsettingsbtn);
        rateApp = v.findViewById(R.id.Rateapp);
        plantArticles = v.findViewById(R.id.PlantArticles);
        findShop = v.findViewById(R.id.FindPlantShop);
        helpCenter = v.findViewById(R.id.HelpCenter);
        appInfo = v.findViewById(R.id.appInfo);

        Intent intent = getActivity().getIntent();
        user_name = intent.getStringExtra("fullname");
        user_number = intent.getStringExtra("number");
        user_image = intent.getStringExtra("imgUrl");

        appInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppInformation newFragment = new AppInformation();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        rateApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppRate.with(getActivity()).showRateDialog(getActivity());
            }
        });
        helpCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HelpCenter newFragment = new HelpCenter();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        findShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().startActivity(new Intent(getContext(), NearbyPlantPlaces.class));
            }
        });
        plantArticles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.hsph.harvard.edu/nutritionsource/what-should-you-eat/vegetables-and-fruits/"));
                startActivity(browserIntent);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Tell your friends about this app!";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Tell your friends about this app");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
        if(user_image!= ""){
            Picasso.with(getContext()).load(user_image).into(profileimg);
        }else {
            profileimg.setImageResource(R.drawable.user_profile);
        }

        fname.setText(user_name);
        number.setText(user_number);
        checuserstatus();
        getSubscription();
        if (premiumbtn.equals("Premium")) {
            premiumbtn.setEnabled(false);
        } else {
            premiumbtn.setEnabled(true);
        }

        new getToken().execute();
        premiumbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitPayment();
            }
        });

        fname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Login.class);
                setLoginState(true);
                startActivity(intent);
            }
        });

        accountsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new AccountSettings()).addToBackStack(null).commit();
            }
        });

        return v;
    }

    private void submitPayment() {
        DropInRequest dropInRequest = new DropInRequest().clientToken(token);
        startActivityForResult(dropInRequest.getIntent(getActivity()), REQUEST_CODE);


    }

    private void sendPayments() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_CHECK_OUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.toString().contains("Successful")) {
                            premiumbtn.setText("Premium");
                            premiumbtn.setBackgroundResource(R.drawable.button_premium);
                            diamond.setVisibility(View.VISIBLE);
                            reference.child(number.getText().toString()).child("subscription").setValue("Premium");
                            Toast.makeText(getActivity(), "Payment Success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Payment Failed", Toast.LENGTH_SHORT).show();
                        }
                        Log.d("Response", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Err", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (paramsHash == null)
                    return null;
                Map<String, String> params = new HashMap<>();
                for (String key : paramsHash.keySet()) {
                    params.put(key, paramsHash.get(key));
                }
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        RetryPolicy mRetryPolicy = new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(mRetryPolicy);
        queue.add(stringRequest);
    }

    private class getToken extends AsyncTask {
//        ProgressDialog mDailog;

        @Override
        protected Object doInBackground(Object[] objects) {
            HttpClient client = new HttpClient();
            client.get(API_GET_TOKEN, new HttpResponseCallback() {
                @Override
                public void success(final String responseBody) {
//                    mDailog.dismiss();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            token = responseBody;
                        }
                    });
                }

                @Override
                public void failure(Exception exception) {
//                    mDailog.dismiss();
                    Log.d("Err", exception.toString());
                }
            });
            return null;
        }

//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            mDailog = new ProgressDialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog);
//            mDailog.setCancelable(false);
//            mDailog.setMessage("Loading, Please Wait");
//            mDailog.show();
//        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodNonce nonce = result.getPaymentMethodNonce();
                String strNounce = nonce.getNonce();

                paramsHash = new HashMap<>();
                paramsHash.put("amount", amount);
                paramsHash.put("nonce", strNounce);

                sendPayments();
            }

        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getActivity(), "User canceled", Toast.LENGTH_SHORT).show();
        } else {
            Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
            Log.d("Err", error.toString());
        }
    }


    //Fetch Data From Firebase
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
        if (counter2 || fname.equals("Log in here")) {
            fname.setText("Log in here");
            accountsettings.setClickable(false);
        }

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("logindata", Context.MODE_PRIVATE);
        Boolean counter = sharedPreferences.getBoolean("logincounter", Boolean.valueOf(String.valueOf(Context.MODE_PRIVATE)));
        String nameupdate = sharedPreferences.getString("fullnameupdate", String.valueOf(Context.MODE_PRIVATE));
        String numberupdate = sharedPreferences.getString("numberupdate", String.valueOf(Context.MODE_PRIVATE));
        String username = sharedPreferences.getString("username", String.valueOf(Context.MODE_PRIVATE));
        String name = sharedPreferences.getString("fullname", String.valueOf(Context.MODE_APPEND));
        String number_user = sharedPreferences.getString("number", String.valueOf(Context.MODE_APPEND));
        String ImageUrl = sharedPreferences.getString("imgUrl", String.valueOf(Context.MODE_APPEND));
        if (counter) {
            fname.setText(name);
            if(ImageUrl!= ""){
                Picasso.with(getContext()).load(ImageUrl).into(profileimg);
            }else {
                profileimg.setImageResource(R.drawable.user_profile);
            }

            number.setText(number_user);
            fname.setClickable(false);

        }else if ( acct!=null){
            accountsettings.setClickable(true);
            String personName = acct.getDisplayName();
            String personMail = acct.getId();
            Uri personImage = acct.getPhotoUrl();
            String toStringImg = personImage.toString();
            fname.setText(personName);
            number.setText(personMail);
            Picasso.with(getContext()).load(toStringImg).into(profileimg);
            fname.setClickable(false);
        }
        else{
            profileimg.setImageResource(R.drawable.user_profile);
        }
    }

    private void getSubscription() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        Query checkUser = reference.orderByChild("number").equalTo(number.getText().toString());
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot childDataSnapshot : snapshot.getChildren()) {
                        childDataSnapshot.getKey();
                        Object pm = childDataSnapshot.child("subscription").getValue();
                        if (pm.equals("Premium")) {
                            premiumbtn.setText("Premium");
                            premiumbtn.setBackgroundResource(R.drawable.button_premium);
                            diamond.setVisibility(View.VISIBLE);
                            premiumbtn.setEnabled(false);
                        } else {
                            premiumbtn.setEnabled(true);
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
        checuserstatus();
        getSubscription();
    }

}