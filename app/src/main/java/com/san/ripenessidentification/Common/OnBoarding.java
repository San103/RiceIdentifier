package com.san.ripenessidentification.Common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Pair;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.san.ripenessidentification.Common.LogInSignup.Login;
import com.san.ripenessidentification.GoogleMaps.NearbyPlantPlaces;
import com.san.ripenessidentification.HelperClasses.SliderAdapter;
import com.san.ripenessidentification.MainActivity;
import com.san.ripenessidentification.R;
import com.san.ripenessidentification.User.UserDashboard;

public class OnBoarding extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout dotsLayout;

    SliderAdapter sliderAdapter;
    TextView[] dots;
    Button getStarted,nextSlide, skipp,sign_in;
    Animation animation;
    int currentPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_on_boarding);

        viewPager = findViewById(R.id.slider);
        dotsLayout = findViewById(R.id.dots);
        getStarted= findViewById(R.id.get_started_btn);
        sign_in= findViewById(R.id.signinButton);
        nextSlide= findViewById(R.id.next_btn);
        skipp= findViewById(R.id.skip_btn);


        sliderAdapter= new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);

        addDots(0);
        viewPager.addOnPageChangeListener(changeListener);
    }
    public void savedata(String username){
        SharedPreferences sharedPreferences=getSharedPreferences("logindata",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("logincounter",false);
        editor.putString("username",username);
        editor.apply();
    }
    public void skip(View view){
        savedata("Log in");
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    public void callLoginScreen(View view){
        Intent intent= new Intent(getApplicationContext(),Login.class);
        Pair[] pairs = new Pair[1];
        pairs[0]= new Pair<View,String>(findViewById(R.id.signinButton),"transition_login");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(OnBoarding.this,pairs);
        startActivity(intent);
    }
    public void next(View view){
        viewPager.setCurrentItem(currentPos + 1);
    }

    private void addDots(int position){

        dots = new TextView[4];
        dotsLayout.removeAllViews();
        for(int i=0; i<dots.length;i++){
            dots[i]= new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);

            dotsLayout.addView(dots[i]);
        }
        if (dots.length>0){
            dots[position].setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    ViewPager.OnPageChangeListener changeListener= new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            currentPos= position;
            if(position==0){
                nextSlide.setVisibility(View.VISIBLE);
                skipp.setVisibility(View.VISIBLE);
                getStarted.setVisibility(View.INVISIBLE);
                sign_in.setVisibility(View.INVISIBLE);
            }else if(position ==1){
                nextSlide.setVisibility(View.VISIBLE);
                skipp.setVisibility(View.VISIBLE);
                getStarted.setVisibility(View.INVISIBLE);
                sign_in.setVisibility(View.INVISIBLE);
            }else if(position ==2){
                nextSlide.setVisibility(View.VISIBLE);
                skipp.setVisibility(View.VISIBLE);
                getStarted.setVisibility(View.INVISIBLE);
                sign_in.setVisibility(View.INVISIBLE);
            }else{
                animation = AnimationUtils.loadAnimation(OnBoarding.this,R.anim.bottom_anim);
                getStarted.setAnimation(animation);
                sign_in.setAnimation(animation);
                skipp.setVisibility(View.INVISIBLE);
                nextSlide.setVisibility(View.INVISIBLE);
                getStarted.setVisibility(View.VISIBLE);
                sign_in.setVisibility(View.VISIBLE);

            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}