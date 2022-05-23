package com.san.ripenessidentification.HelperClasses.HomeAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.san.ripenessidentification.Admin.HelperClasses.SnapHistory;
import com.san.ripenessidentification.R;

import java.util.ArrayList;

public class HistoryAdapter extends FirebaseRecyclerAdapter<
        SnapHistory, HistoryAdapter.personsViewholder> {

    public ArrayList<SnapHistory> arrayList;

    public HistoryAdapter(
            @NonNull FirebaseRecyclerOptions<SnapHistory> options, ArrayList<SnapHistory> arrayList)
    {
        super(options);
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

    // Function to bind the view in Card view(here
    // "person.xml") iwth data in
    // model class(here "person.class")
    @Override
    protected void onBindViewHolder(@NonNull personsViewholder personsViewholder, int i, @NonNull SnapHistory snapHistory) {
        personsViewholder.title.setText(snapHistory.getDate());
        Glide.with(personsViewholder.img.getContext()).load(snapHistory.getImageUrl()).into(personsViewholder.img);

    }

    // Function to tell the class about the Card view (here
    // "person.xml")in
    // which the data will be shown
    @NonNull
    @Override
    public personsViewholder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.history_design,parent,false);
        return new HistoryAdapter.personsViewholder(view);
    }



    // Sub Class to create references of the views in Crad
    // view (here "person.xml")
    class personsViewholder
            extends RecyclerView.ViewHolder {
        public ShapeableImageView img;
        public TextView title;
        public personsViewholder(@NonNull View itemView)
        {
            super(itemView);

            img=(ShapeableImageView)itemView.findViewById(R.id.history_img);
            title = (TextView)itemView.findViewById(R.id.history_time);
        }
    }
}
