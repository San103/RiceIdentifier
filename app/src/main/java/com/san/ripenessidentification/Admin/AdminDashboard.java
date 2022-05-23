
package com.san.ripenessidentification.Admin;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;

        import android.content.ContentResolver;
        import android.content.Intent;
        import android.net.Uri;
        import android.os.Bundle;
        import android.provider.ContactsContract;
        import android.view.View;
        import android.webkit.MimeTypeMap;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.ProgressBar;
        import android.widget.Toast;

        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.material.textfield.TextInputLayout;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
        import com.google.firebase.storage.FirebaseStorage;
        import com.google.firebase.storage.OnProgressListener;
        import com.google.firebase.storage.StorageReference;
        import com.google.firebase.storage.UploadTask;
        import com.san.ripenessidentification.Admin.UpdateDelete.FeaturedAdminDeleteUpdate;
        import com.san.ripenessidentification.Admin.UpdateDelete.FeaturedEdit;
        import com.san.ripenessidentification.R;
        import com.san.ripenessidentification.Admin.HelperClasses.Model;
        import com.san.ripenessidentification.Admin.HelperClasses.PlantInfo;


public class AdminDashboard extends AppCompatActivity {

    //widgets


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

    }

    public void featuredbtn(View v){
        Intent intent = new Intent(getApplicationContext(), FeaturedAdmin.class);
        startActivity(intent);
        finish();
    }
    public void diagnoseroute(View v) {
        Intent intent = new Intent(getApplicationContext(), diagnoseactivity.class);
        startActivity(intent);
        finish();
    }
    public void mvroute(View v) {
        Intent intent = new Intent(getApplicationContext(), MostViewedAdmin.class);
        startActivity(intent);
        finish();
    }

}