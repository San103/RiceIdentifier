package com.san.ripenessidentification.Admin.UpdateDelete;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;

import com.san.ripenessidentification.FragmentMain.HomeFragment;
import com.san.ripenessidentification.R;

public class FeaturedAdminDeleteUpdate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_featured_admin_delete_update);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentAdmin, FeaturedEdit.class,null)
                .commit();

    }
}