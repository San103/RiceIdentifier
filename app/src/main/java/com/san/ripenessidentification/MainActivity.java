package com.san.ripenessidentification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.san.ripenessidentification.FragmentMain.HomeFragment;
import com.san.ripenessidentification.FragmentMain.LikesFragment;
import com.san.ripenessidentification.FragmentMain.NotifFragment;
import com.san.ripenessidentification.FragmentMain.ProfileFragment;
import com.san.ripenessidentification.FragmentMain.ScanFragment;

public class MainActivity extends AppCompatActivity {

    private int selectedTab =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);




        final LinearLayout homeLayout =findViewById(R.id.homeLayout);
        final LinearLayout likeLayout =findViewById(R.id.likeLayout);
//        final LinearLayout scanLayout =findViewById(R.id.scanLayout);
        final LinearLayout notifLayout =findViewById(R.id.notifLayout);
        final LinearLayout profileLayout =findViewById(R.id.profileLayout);

        final ImageView homeImage = findViewById(R.id.homeImage);
        final ImageView likeImage = findViewById(R.id.likeImage);
//        final ImageView scanImage = findViewById(R.id.scanImage);
        final ImageView notifImage = findViewById(R.id.notifImage);
        final ImageView profileImage = findViewById(R.id.profileImage);

        final TextView homeTxt =findViewById(R.id.homeTxt);
        final TextView likeTxt =findViewById(R.id.likeTxt);
//        final TextView scanTxt =findViewById(R.id.scanTxt);
        final TextView notifTxt =findViewById(R.id.notifTxt);
        final TextView profileTxt =findViewById(R.id.profileTxt);

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainer, HomeFragment.class,null)
                .commit();

        homeLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //check if home is selected
                if(selectedTab !=1){

                    //set Home fragment
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, HomeFragment.class,null)
                            .commit();

                    //unselect other tabs except home tab
                    likeTxt.setVisibility(View.GONE);
                    notifTxt.setVisibility(View.GONE);
//                    scanTxt.setVisibility(View.GONE);
                    profileTxt.setVisibility(View.GONE);

                    likeImage.setImageResource(R.drawable.scan_icon);
                    notifImage.setImageResource(R.drawable.notification_icon);
//                    scanImage.setImageResource(R.drawable.scan_icon);
                    profileImage.setImageResource(R.drawable.profile_icon);

                    likeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    notifLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
//                    scanLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    profileLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    //select Home Tab
                    homeTxt.setVisibility(View.VISIBLE);
                    homeImage.setImageResource(R.drawable.home_selected);
                    homeLayout.setBackgroundResource(R.drawable.round_back_home_100);

                    //animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF,0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    homeLayout.startAnimation(scaleAnimation);
                    //set 1st tab as selected
                    selectedTab =1;

                }
            }
        });

        likeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedTab !=2){

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, LikesFragment.class,null)
                            .commit();
                    //unselect other tabs except home tab
                    homeTxt.setVisibility(View.GONE);
                    notifTxt.setVisibility(View.GONE);
                    profileTxt.setVisibility(View.GONE);
//                    scanTxt.setVisibility(View.GONE);

                    homeImage.setImageResource(R.drawable.home_icon);
                    notifImage.setImageResource(R.drawable.notification_icon);
                    profileImage.setImageResource(R.drawable.profile_icon);
//                    scanImage.setImageResource(R.drawable.scan_icon);

                    homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    notifLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
//                    scanLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    profileLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    //select Home Tab
                    likeTxt.setVisibility(View.VISIBLE);
                    likeImage.setImageResource(R.drawable.scan_icon);
                    likeLayout.setBackgroundResource(R.drawable.round_back_like_100);

                    //animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,1.0f, Animation.RELATIVE_TO_SELF,0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    likeLayout.startAnimation(scaleAnimation);
                    //set 1st tab as selected
                    selectedTab =2;

                }
            }
        });
//        scanLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(selectedTab !=3){
//
//                    getSupportFragmentManager().beginTransaction()
//                            .setReorderingAllowed(true)
//                            .replace(R.id.fragmentContainer, ScanFragment.class,null)
//                            .commit();
//
//                    //unselect other tabs except home tab
//                    homeTxt.setVisibility(View.GONE);
//                    notifTxt.setVisibility(View.GONE);
//                    likeTxt.setVisibility(View.GONE);
//                    profileTxt.setVisibility(View.GONE);
//
//                    homeImage.setImageResource(R.drawable.home_icon);
//                    notifImage.setImageResource(R.drawable.notification_icon);
//                    likeImage.setImageResource(R.drawable.diagnose1);
//                    profileImage.setImageResource(R.drawable.profile_icon);
//
//                    homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
//                    notifLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
//                    likeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
//                    profileLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
//
//                    //select Home Tab
//                    scanTxt.setVisibility(View.VISIBLE);
//                    scanImage.setImageResource(R.drawable.scan_selected);
//                    scanLayout.setBackgroundResource(R.drawable.round_back_scan_100);
//
//                    //animation
//                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,1.0f, Animation.RELATIVE_TO_SELF,0.0f);
//                    scaleAnimation.setDuration(200);
//                    scaleAnimation.setFillAfter(true);
//                    scanLayout.startAnimation(scaleAnimation);
//                    //set 1st tab as selected
//                    selectedTab =3;
//
//                }
//            }
//        });

        notifLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedTab !=3){

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, NotifFragment.class,null)
                            .commit();
                    //unselect other tabs except home tab
                    homeTxt.setVisibility(View.GONE);
                    likeTxt.setVisibility(View.GONE);
                    profileTxt.setVisibility(View.GONE);
//                    scanTxt.setVisibility(View.GONE);

                    homeImage.setImageResource(R.drawable.home_icon);
                    likeImage.setImageResource(R.drawable.scan_icon);
                    profileImage.setImageResource(R.drawable.profile_icon);
//                    scanImage.setImageResource(R.drawable.scan_icon);

                    homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    likeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    profileLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
//                    scanLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    //select Home Tab
                    notifTxt.setVisibility(View.VISIBLE);
                    notifImage.setImageResource(R.drawable.notification_selected);
                    notifLayout.setBackgroundResource(R.drawable.round_back_notification_100);

                    //animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,1.0f, Animation.RELATIVE_TO_SELF,0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    notifLayout.startAnimation(scaleAnimation);
                    //set 1st tab as selected
                    selectedTab =3;

                }
            }
        });

        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedTab !=4){

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, ProfileFragment.class,null)
                            .commit();
                    //unselect other tabs except home tab
                    homeTxt.setVisibility(View.GONE);
                    likeTxt.setVisibility(View.GONE);
                    notifTxt.setVisibility(View.GONE);
//                    scanTxt.setVisibility(View.GONE);

                    homeImage.setImageResource(R.drawable.home_icon);
                    likeImage.setImageResource(R.drawable.scan_icon);
//                    scanImage.setImageResource(R.drawable.scan_icon);
                    notifImage.setImageResource(R.drawable.notification_icon);

                    homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    likeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    notifLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
//                    scanLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    //select Home Tab
                    profileTxt.setVisibility(View.VISIBLE);
                    profileImage.setImageResource(R.drawable.profile_selected);
                    profileLayout.setBackgroundResource(R.drawable.round_back_profile_100);

                    //animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,1.0f, Animation.RELATIVE_TO_SELF,0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    profileLayout.startAnimation(scaleAnimation);
                    //set 1st tab as selected
                    selectedTab =4;

                }
            }
        });
    }
}