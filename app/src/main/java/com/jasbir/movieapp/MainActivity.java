package com.jasbir.movieapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jasbir.movieapp.Adapter.CustomCursorAdapter;
import com.jasbir.movieapp.Adapter.ImageViewAdapter;
import com.jasbir.movieapp.Network.NetworkUtils;
import com.example.movieapp.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity  {
    private static final int TASK_LODER_ID = 22;
    public final String DEFAULT_QUERY = "https://api.themoviedb.org/3/movie/popular?api_key=enter your api key here &language=en-US";
    public final String TOP_RATED_QUERY = "https://api.themoviedb.org/3/movie/top_rated?api_key=enter your api key here &language=en-US";

    ProgressBar progressBar;
    ArrayList<MovieData> data;
    MovieData movieData;
    RecyclerView recyclerView;
    ImageViewAdapter adapter;
    URL url;
    CustomCursorAdapter customCursorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressbar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Movies");
        //Setting up default url;
        url=null;
        try{
            url = new URL(DEFAULT_QUERY);
        }
        catch(Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        recyclerView = findViewById(R.id.recyclerView);
        FetchData fetchData = new FetchData();
        fetchData.execute(url);
    }



    public class FetchData extends AsyncTask<URL,Void,String>{
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            String result=null;
            try {
                result = NetworkUtils.getResponseFromUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.INVISIBLE);
            if(s!=null && !s.equals(""))
                ParseJson(s);
            else{
                View parentLayout = MainActivity.this.findViewById(R.id.root);
                Snackbar.make(parentLayout,"Please check internet connection!!",Snackbar.LENGTH_INDEFINITE)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                FetchData fetchData = new FetchData();
                                fetchData.execute(url);
                            }
                        }).show();
            }
        }
    }

    private void ParseJson(String s) {
        try {
            data = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jArray = jsonObject.getJSONArray("results");
            for(int i=0;i<jArray.length();i++){
                JSONObject currentObject = jArray.getJSONObject(i);
                movieData = new MovieData();
                movieData.setTitle(currentObject.getString("title"));
                movieData.setPoster(currentObject.getString("poster_path"));
                movieData.setRelaeseData(currentObject.getString("release_date"));
                movieData.setPlot_synopsis(currentObject.getString("overview"));
                movieData.setVote_avarage(currentObject.getString("vote_average"));
                movieData.setMovieId(currentObject.getString("id"));
                data.add(movieData);
//                Toast.makeText(this, currentObject.getString("title"), Toast.LENGTH_LONG).show();
//                Toast.makeText(this, currentObject.getString("overview"), Toast.LENGTH_LONG).show();
            }

            adapter = new ImageViewAdapter(data,this);
            recyclerView.setLayoutManager(new GridLayoutManager(this,numberofColumn()));
            recyclerView.setAdapter(adapter);

        } catch (JSONException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private int numberofColumn(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if(nColumns<2)return 2;
        return nColumns;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FetchData fetchData = new FetchData();
        URL url=null;
        if(item.getItemId() == R.id.popular){
            try {
                url = new URL(DEFAULT_QUERY);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        else if(item.getItemId() == R.id.rated){
            try {
                url = new URL(TOP_RATED_QUERY);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        else if(item.getItemId() == R.id.about){
            //Retrieve all data from the content provider in background thread using AsyncTask Loader
            //Use custom cursor adapter to show the images in imageview
          startActivity(new Intent(MainActivity.this,AboutActivity.class));

        }
        if(url!=null){
            fetchData.execute(url);
            return true;
        }
        return  false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
}
