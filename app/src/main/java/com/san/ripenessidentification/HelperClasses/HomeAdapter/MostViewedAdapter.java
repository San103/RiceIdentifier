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

public class MostViewedAdapter extends RecyclerView.Adapter<MostViewedAdapter.myviewholder> {

    public Context c;
    public ArrayList<MostViewedHelperClass> arrayList;
    public MostViewedAdapter(Context c, ArrayList<MostViewedHelperClass> arrayList){
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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.most_viewed,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        MostViewedHelperClass mostViewedHelperClass=arrayList.get(position);
        holder.title.setText(mostViewedHelperClass.getName());
        holder.desc.setText(mostViewedHelperClass.getDescription());
        Glide.with(holder.img.getContext()).load(mostViewedHelperClass.getImgUrl()).into(holder.img);
    }




    public static class myviewholder extends RecyclerView.ViewHolder{
        public ImageView img;
        public TextView title,desc;
        public myviewholder(View itemView){
            super(itemView);
            img =(ImageView)itemView.findViewById(R.id.mv_img);
            title = (TextView)itemView.findViewById(R.id.mv_title);
            desc = (TextView)itemView.findViewById(R.id.mv_desc);


        }
    }
}
