package com.jasbir.movieapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import androidx.recyclerview.widget.RecyclerView;

import com.jasbir.movieapp.DetailActivity;
import com.example.movieapp.R;
import com.jasbir.movieapp.data.FavoriteContract;


/**
 * Created by Jasbir Singh
 */

public class CustomCursorAdapter extends RecyclerView.Adapter<CustomCursorAdapter.MyCursorViewHolder>{
    Cursor mData;
    Context context;
    public CustomCursorAdapter(Cursor data, Context con){
        mData = data;
        context = con;
    }

    @Override
    public MyCursorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.imagelayout,parent,false);
        MyCursorViewHolder viewHolder = new MyCursorViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyCursorViewHolder holder, int position) {
        mData.moveToPosition(position);
        String imageLocation = mData.getString(mData.getColumnIndex(FavoriteContract.FavoriteEntry.IMAGESTORAGELOCATION_COLUMN));
        holder.imageView.setImageURI(Uri.parse(imageLocation));

        //Set click listner for the imageView in favorite
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                mData.moveToPosition(pos);
                Intent i = new Intent(context, DetailActivity.class);
                i.putExtra("title",mData.getString(mData.getColumnIndex(FavoriteContract.FavoriteEntry.MOVIENAME_COLUMN)));
                i.putExtra("release",mData.getString(mData.getColumnIndex(FavoriteContract.FavoriteEntry.RELEASEDATE_COLUMN)));
                i.putExtra("rating",mData.getString(mData.getColumnIndex(FavoriteContract.FavoriteEntry.RATING_COLUMN)));
                i.putExtra("poster",mData.getString(mData.getColumnIndex(FavoriteContract.FavoriteEntry.IMAGESTORAGELOCATION_COLUMN)));
                i.putExtra("overview",mData.getString(mData.getColumnIndex(FavoriteContract.FavoriteEntry.OVERVIEW_COLUMN)));
                context.startActivity(i);
            }
        });
        //start details activity and show all the stored details there
    }

    @Override
    public int getItemCount() {
        return mData.getCount();
    }

    public class MyCursorViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        public MyCursorViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
