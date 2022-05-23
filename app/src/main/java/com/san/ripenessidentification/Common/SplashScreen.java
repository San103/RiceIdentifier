package com.san.ripenessidentification.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.san.ripenessidentification.MainActivity;
import com.san.ripenessidentification.R;
import com.san.ripenessidentification.User.UserDashboard;

public class SplashScreen extends AppCompatActivity {

    public static int SPLASH_TIMER = 2000;

    //Variables
    ImageView BackgroundImage;
    TextView PoweredBy;

    //animations
    Animation sideAnim,bottomAnim;
    SharedPreferences onBoardingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splash_screen);

        //Hooks
        BackgroundImage =findViewById(R.id.background_image);
        PoweredBy =findViewById(R.id.powered_by_line);

        //Animations
        sideAnim = AnimationUtils.loadAnimation(this,R.anim.side_anim);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_anim);

        //set Animations
        BackgroundImage.setAnimation(sideAnim);
        PoweredBy.setAnimation(bottomAnim);

        //BackgroundImage.animate().translationX(-2500).setDuration(1000).setStartDelay(4000);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {


                onBoardingScreen = getSharedPreferences("onBoardingScreen", MODE_PRIVATE);
                boolean isFirstTime = onBoardingScreen.getBoolean("firstTime",true);

                if(isFirstTime){
                    SharedPreferences.Editor editor=onBoardingScreen.edit();
                    editor.putBoolean("firstTime",false);
                    editor.commit();

                    Intent intent = new Intent(SplashScreen.this, OnBoarding.class);
                    startActivity(intent);
                    finish();
                }else{

                //UserDashboard
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }


            }
        },SPLASH_TIMER);
    }
    public void savedata(String username){
        SharedPreferences sharedPreferences=getSharedPreferences("logindata",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("logincounter",true);
        editor.putString("username",username);
        editor.apply();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        intent.putExtra("fullname", namefromdb);
        startActivity(intent);
    }
}