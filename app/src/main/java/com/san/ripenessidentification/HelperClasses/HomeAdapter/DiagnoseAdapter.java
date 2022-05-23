package com.san.ripenessidentification.HelperClasses.HomeAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.san.ripenessidentification.FragmentMain.Home.descfragment;
import com.san.ripenessidentification.FragmentMain.Home.diagnosedescfragment;
import com.san.ripenessidentification.R;

import java.util.ArrayList;

public class DiagnoseAdapter extends RecyclerView.Adapter<DiagnoseAdapter.myviewholder>{

    public Context c;
    public ArrayList<DiagnoseHelperClass> arrayList;

    public DiagnoseAdapter(Context c, ArrayList<DiagnoseHelperClass> arrayList){
        this.c=c;
        this.arrayList=arrayList;
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.diagnose_design,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        DiagnoseHelperClass diagnoseHelperClass=arrayList.get(position);
        holder.title.setText(diagnoseHelperClass.getDiagname());
        holder.desc.setText(diagnoseHelperClass.getDiagdescription());
        Glide.with(holder.img.getContext()).load(diagnoseHelperClass.getImgUrl()).into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity=(AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new diagnosedescfragment(diagnoseHelperClass.getDiagname(),diagnoseHelperClass.getDiagdescription(),diagnoseHelperClass.getImgUrl())).addToBackStack(null).commit();

            }
        });
    }

    public static class myviewholder extends RecyclerView.ViewHolder{
        public ImageView img;
        public TextView title,desc;
        public myviewholder(View itemView){
            super(itemView);
            img=(ImageView)itemView.findViewById(R.id.diagnose_image);
            title = (TextView)itemView.findViewById(R.id.diagnose_title);
            desc = (TextView)itemView.findViewById(R.id.diagnose_desc);


        }
    }
}
