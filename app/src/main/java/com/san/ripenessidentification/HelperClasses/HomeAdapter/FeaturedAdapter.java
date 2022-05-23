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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.san.ripenessidentification.FragmentMain.Home.descfragment;
import com.san.ripenessidentification.R;

import java.util.ArrayList;

public class FeaturedAdapter extends RecyclerView.Adapter<FeaturedAdapter.myviewholder> {

    public Context c;
    public ArrayList<FeaturedHelperClass> arrayList;
    public FeaturedAdapter(Context c, ArrayList<FeaturedHelperClass> arrayList){
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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.featured_design,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        FeaturedHelperClass featuredHelperClass=arrayList.get(position);
        holder.title.setText(featuredHelperClass.getPlantname());
        holder.desc.setText(featuredHelperClass.getDescription());
        Glide.with(holder.img.getContext()).load(featuredHelperClass.getImageUrl()).into(holder.img);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity=(AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new descfragment(featuredHelperClass.getPlantname(),featuredHelperClass.getDescription(),featuredHelperClass.getImageUrl(),featuredHelperClass.getFertilizer(),featuredHelperClass.getSunlight(),featuredHelperClass.getWatering())).addToBackStack(null).commit();
            }
        });
    }




    public static class myviewholder extends RecyclerView.ViewHolder{
        public ImageView img;
        public TextView title,desc;
        public myviewholder(View itemView){
            super(itemView);
            img=(ImageView)itemView.findViewById(R.id.featured_image);
            title = (TextView)itemView.findViewById(R.id.featured_title);
            desc = (TextView)itemView.findViewById(R.id.featured_desc);


        }
    }
}
