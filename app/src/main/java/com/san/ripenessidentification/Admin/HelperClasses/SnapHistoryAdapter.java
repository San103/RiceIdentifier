package com.san.ripenessidentification.Admin.HelperClasses;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.san.ripenessidentification.FragmentMain.Home.descfragment;
import com.san.ripenessidentification.HelperClasses.HomeAdapter.FeaturedAdapter;
import com.san.ripenessidentification.HelperClasses.HomeAdapter.FeaturedHelperClass;
import com.san.ripenessidentification.R;

import java.util.ArrayList;

public class SnapHistoryAdapter extends RecyclerView.Adapter<SnapHistoryAdapter.myviewholder> {

    public Context c;
    public ArrayList<SnapHistory> arrayList;
    public SnapHistoryAdapter(Context c, ArrayList<SnapHistory> arrayList){
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
    public SnapHistoryAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.history_design,parent,false);
        return new SnapHistoryAdapter.myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
//        SnapHistory snapHistory=arrayList.get(position);
//        holder.title.setText(snapHistory.getDate());
//        Glide.with(holder.img.getContext()).load(snapHistory.getImageUrl()).into(holder.img);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AppCompatActivity activity=(AppCompatActivity)view.getContext();
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new descfragment(featuredHelperClass.getPlantname(),featuredHelperClass.getDescription(),featuredHelperClass.getImageUrl(),featuredHelperClass.getFertilizer(),featuredHelperClass.getSunlight(),featuredHelperClass.getWatering())).addToBackStack(null).commit();
//            }
//        });
    }





    public static class myviewholder extends RecyclerView.ViewHolder{
        public ShapeableImageView img;
        public ImageView deleteHistory;
        public TextView title;
        public myviewholder(View itemView){
            super(itemView);
            img=(ShapeableImageView)itemView.findViewById(R.id.history_img);
            title = (TextView)itemView.findViewById(R.id.history_time);
            deleteHistory = (ImageView) itemView.findViewById(R.id.deleteHistory);


        }
    }
}
