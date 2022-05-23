package com.san.ripenessidentification.Admin.HelperClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.san.ripenessidentification.R;

import java.util.ArrayList;

public class ScanHistoryAdapter extends RecyclerView.Adapter<ScanHistoryAdapter.myviewholder>{

    public Context c;
    public ArrayList<ScanHistory> arrayList;

    public ScanHistoryAdapter(Context c, ArrayList<ScanHistory> arrayList){
        this.c=c;
        this.arrayList=arrayList;
    }

    @NonNull
    @Override
    public ScanHistoryAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.history_design,parent,false);
        return new ScanHistoryAdapter.myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScanHistoryAdapter.myviewholder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
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
