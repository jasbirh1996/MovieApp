package com.jasbir.movieapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.recyclerview.widget.RecyclerView;

import com.jasbir.movieapp.DetailActivity;
import com.jasbir.movieapp.MovieData;
import com.example.movieapp.R;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Jasbir Singh
 */

public class ImageViewAdapter extends RecyclerView.Adapter<ImageViewAdapter.MyViewHolder> {

    ArrayList<MovieData> dataList;
    Context c;
    public ImageViewAdapter(ArrayList<MovieData> list, Context context){
        dataList = list;
        c = context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(c).inflate(R.layout.imagelayout,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Picasso.Builder builder = new Picasso.Builder(c).memoryCache(new LruCache(24000));
        builder.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                Toast.makeText(c, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Picasso pic = builder.build();
        pic.load("https://image.tmdb.org/t/p/w500"+dataList.get(position).getPoster()).error(R.drawable.error).into(holder.imageView);
//        Picasso.with(c).load("https://image.tmdb.org/t/p/w500"+dataList.get(position).getPoster()).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(c, "Item Clicked at " + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                Intent i= new Intent(c, DetailActivity.class);
                i.putExtra("id",dataList.get(position).getMovieId());
                i.putExtra("title",dataList.get(position).getTitle());
                i.putExtra("release",dataList.get(position).getRelaeseData());
                i.putExtra("rating",dataList.get(position).getVote_avarage());
                i.putExtra("poster",dataList.get(position).getPoster());
                i.putExtra("overview",dataList.get(position).getPlot_synopsis());
                c.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.imageView);
        }
    }

}
