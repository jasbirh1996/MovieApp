package com.jasbir.movieapp;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jasbir.movieapp.Adapter.ReviewAdapter;
import com.jasbir.movieapp.Adapter.TrailerAdapter;

import com.example.movieapp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/***
 * created by Jasbir singh...
 */

public class DetailActivity extends AppCompatActivity {
    // private static final int EXTERNAL_PERMISSION_CODE = 2;
    String title, release, rating, overview, poster, movieId;
    ArrayList<String> trailerDetials, reviewDetails;
    TrailerAdapter trailerAdapter;
    ReviewAdapter reviewAdapter;
    ProgressBar progressBar;

    TextView overviewTv;
    TextView titleTv;
    TextView releaseTv;
    TextView ratingTv;
    ImageView posterImageView;
    RecyclerView trailerRv;
    RecyclerView reviewRv;

    RelativeLayout relativeLayout;
    TextView textViewReview;

    TextView trailerTv;
    String trailer_query, review_query;
    RequestQueue queue;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Movie Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetailActivity.this, MainActivity.class));
            }
        });

        overviewTv = findViewById(R.id.textViewDescription);
        titleTv = findViewById(R.id.textViewTitle);
        releaseTv = findViewById(R.id.textViewYearoflaunch);
        ratingTv = findViewById(R.id.textViewRate);
        posterImageView = findViewById(R.id.imageViewMovieposter);

        trailerRv = findViewById(R.id.trailersRecyclerView);
        reviewRv = findViewById(R.id.reviewRecyclerView);

        relativeLayout = findViewById(R.id.detailRoot);
        textViewReview = findViewById(R.id.textViewReview);
        trailerTv = findViewById(R.id.textViewTrailers);


        progressBar = new ProgressBar(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        relativeLayout.addView(progressBar, params);


        progressBar.setVisibility(View.VISIBLE);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null)
            actionBar.show();
        if (getIntent().hasExtra("title")) {
            Bundle bundle = getIntent().getExtras();
            title = String.valueOf(bundle.get("title"));
            release = "Released Date: " + String.valueOf(bundle.get("release"));
            rating = "Rank : " + String.valueOf(bundle.get("rating"));
            overview = String.valueOf(bundle.get("overview"));
            poster = String.valueOf(bundle.get("poster"));
            movieId = String.valueOf(bundle.get("id"));
        }

        //On start of the detail activity, check that the particular movie is favorite or not
        //on the basis of result show the button text according to that
      /*  favoriteFactor = isFavorite(title);
        if(favoriteFactor){
            favoriteBtn.setText(getString(R.string.unfavoriteButtonText));
        }
        else {
            favoriteBtn.setText(getString(R.string.favoriteButtonText));
        } */

        //When we access the favorite data, the movie id will be null due to which
        //we getTrailer and getReviews wouldn't be called because we might be offline at that moment
        if (!movieId.equals("null")) {
            getTrailers(movieId);
            getReviews(movieId);
        }
        titleTv.setText(title);
        releaseTv.setText(release);
        ratingTv.setText(rating);
        overviewTv.setText(overview);

        //If user want to access from favorite database then there is no need to download the image from internet
        //since we have already saved the imageLocation in database and when user come to this activity via
        //favorite list then the poster variable contains the complete path of image inside of smart phone
        if (!movieId.equals("null"))
            Picasso.get().load(getString(R.string.image_url) + poster).error(R.drawable.error).into(posterImageView);
        else {
            posterImageView.setImageURI(Uri.parse(poster));
            progressBar.setVisibility(View.INVISIBLE);
        }


    }

    private void getReviews(String movieId) {
        review_query = "https://api.themoviedb.org/3/movie/" + movieId + "/reviews?api_key=enter your api key here";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, review_query, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null && !response.equals("")) {
                    ParseReviewJson(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(stringRequest);
    }

    private void ParseReviewJson(String response) {
        try {
            reviewDetails = new ArrayList<>();
            JSONObject root = new JSONObject(response);
            JSONArray resultArray = root.getJSONArray("results");
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject currentObject = resultArray.getJSONObject(i);
                String content = currentObject.getString("content");
                String reviewer = currentObject.getString("author");
                //Since I have to store both information
                String concatednatedString = content + "`" + reviewer;
                reviewDetails.add(concatednatedString);

            }
            if (resultArray.length() == 0) {
                textViewReview.setText(getString(R.string.noReviewAvailable));
                progressBar.setVisibility(View.INVISIBLE);
                return;
            }
            reviewAdapter = new ReviewAdapter(reviewDetails, this);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            reviewRv.setLayoutManager(linearLayoutManager);
            reviewRv.setAdapter(reviewAdapter);
            progressBar.setVisibility(View.INVISIBLE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getTrailers(String movieId) {
//        progressBar.setVisibility(View.VISIBLE);
        trailer_query = "https://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key=enter your api key here ";
        queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, trailer_query, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null && !response.equals("")) {
                    ParseJson(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(DetailActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }

    private void ParseJson(String res) {
        try {
            trailerDetials = new ArrayList<>();
            JSONObject root = new JSONObject(res);
            JSONArray resultArray = root.getJSONArray("results");
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject currentObject = resultArray.getJSONObject(i);
                String youtubeKeyLink = currentObject.getString("key");
                trailerDetials.add(youtubeKeyLink);
            }
            if (resultArray.length() == 0) {
                trailerTv.setText(getString(R.string.noTrailerAvailable));
                return;
            }
//            progressBar.setVisibility(View.INVISIBLE);
            trailerAdapter = new TrailerAdapter(trailerDetials, this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            trailerRv.setLayoutManager(layoutManager);
            trailerRv.setAdapter(trailerAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}  /*
    public void askDownloadingPermissions(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},EXTERNAL_PERMISSION_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == EXTERNAL_PERMISSION_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            downloadImage();
        }
        else{
            Toast.makeText(this, "Please give permission to make it favorite", Toast.LENGTH_SHORT).show();
        }
    }
    public void downloadImage(){
        Picasso.get().load(getString(R.string.image_url)+ poster).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                File file = new File(Environment.getExternalStorageDirectory().getPath()
                        +"/" + title + ".jpg");
                imageLocation = file.getPath();
                try {
                    file.createNewFile();
                    FileOutputStream outputStream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,75,outputStream);
                    outputStream.close();
//                    Toast.makeText(MovieDetailActivity.this, "Downloaded", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Toast.makeText(DetailActivity.this, "Some thing went wrong..", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });
        //Store the details to view it offline
        if(imageLocation!=null && !imageLocation.equals(""))
            insertion = AddToFavorite(title,release,rating,imageLocation,overview);
        if(insertion != -1){
            //insertion has been successfully done
            Toast.makeText(this, "Marked as favorite", Toast.LENGTH_SHORT).show();
            //change the text to unfavoriteText
            favoriteBtn.setText(getString(R.string.unfavoriteButtonText));
        }
        else{
            Toast.makeText(this, "There is some problem, please try again", Toast.LENGTH_SHORT).show();
        }
    }
    @OnClick(R.id.button_favorite)
    @Override
    public void onClick(View view) {
        if(view == favoriteBtn){
            if(favoriteBtn.getText() == getString(R.string.favoriteButtonText)) {
                //download the image into smartphone and save the location in database to view it offline
                if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                    askDownloadingPermissions();
                else {
                    downloadImage();
                }
            }
            else{
                deletion = 0;
                //remove the detail of that particular movie
                try{
                    deletion = RemoveFromFavorite(title);
                }
                catch (Exception e){
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                if(deletion>0){
                    //deleted successfully
                    Toast.makeText(this, "Remove from your favorite list", Toast.LENGTH_SHORT).show();
                    //check weather we are at favorite list or some other list
                    if(movieId.equals("null")){
                        //we are in favorite view
                        startActivity(new Intent(this,MainActivity.class));
                        finish();
                    }
                    //change the text to favoriteText
                    favoriteBtn.setText(getString(R.string.favoriteButtonText));
                }
                //delete it's image from smartphone
                File file = new File(Environment.getExternalStorageDirectory().getPath()
                        +"/" + title + ".jpg");
                if(file.exists())
                    file.delete();
            }
        }
    }
    private long RemoveFromFavorite(String title) {
//        return myDatabase.delete(FavoriteContract.FavoriteEntry.TABLE_NAME
//        , FavoriteContract.FavoriteEntry.MOVIENAME_COLUMN + "='" + title + "'",null);
        return getContentResolver().delete(FavoriteContract.FavoriteEntry.CONTENT_URI,
                FavoriteContract.FavoriteEntry.MOVIENAME_COLUMN + "='" + title + "'",null);
    }
    public boolean isFavorite(String title){
//        Cursor cursor = myDatabase.query(FavoriteContract.FavoriteEntry.TABLE_NAME,
//                null,null,null,null,null,null);
        Cursor cursor = getContentResolver().query(FavoriteContract.FavoriteEntry.CONTENT_URI
                ,null,null,null,null);
        String factor = null;
        while(cursor.moveToNext()){
            factor = cursor.getString(1);
            if(factor.equals(title)){
                return true;
            }
        }
        return false;
    }
    public long AddToFavorite(String mName,String rDate,String rate,String imgLocation, String overV){
        ContentValues cv = new ContentValues();
        cv.put(FavoriteContract.FavoriteEntry.MOVIENAME_COLUMN,mName);
        cv.put(FavoriteContract.FavoriteEntry.RELEASEDATE_COLUMN,rDate);
        cv.put(FavoriteContract.FavoriteEntry.RATING_COLUMN,rate);
        cv.put(FavoriteContract.FavoriteEntry.IMAGESTORAGELOCATION_COLUMN,imgLocation);
        cv.put(FavoriteContract.FavoriteEntry.OVERVIEW_COLUMN,overV);
//        return myDatabase.insert(FavoriteContract.FavoriteEntry.TABLE_NAME,null,cv);
        Uri uri = getContentResolver().insert(FavoriteContract.FavoriteEntry.CONTENT_URI,cv);
        if(uri!=null) {
            return 0;
        }
        return -1;
    }
}
*/

