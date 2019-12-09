package com.jasbir.movieapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.example.movieapp.R;

import java.util.ArrayList;

/**
 * Created by Jasbir singh...
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    ArrayList<String>linksData;
    Context context;
    public TrailerAdapter(ArrayList<String> link,Context c){
        linksData = link;
        context = c;

    }
    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_layout,parent,false);
        TrailerViewHolder viewHolder = new TrailerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final TrailerViewHolder holder, int position) {


        holder.trailerTv.setText(context.getString(R.string.trailer_label) + " " + (holder.getAdapterPosition()+1));
        holder.trailerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //I am using getAdapterposition from viewHolder because the onBindViewHolder is called one after another so
                //the position is updated according to the last call of onBindViewHolder which causes problem when user click on
                //the old view and the position he get is of the latest view
                String youtubeKey = linksData.get(holder.getAdapterPosition());
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+youtubeKey)));
            }
        });

    }

    @Override
    public int getItemCount() {
        return linksData.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder{
        ImageView trailerIcon;
        TextView trailerTv;
        public TrailerViewHolder(View itemView) {
            super(itemView);
            trailerIcon = (ImageView)itemView.findViewById(R.id.imageViewPlay);
            trailerTv = (TextView)itemView.findViewById(R.id.trailerTextView);
        }
    }
}
