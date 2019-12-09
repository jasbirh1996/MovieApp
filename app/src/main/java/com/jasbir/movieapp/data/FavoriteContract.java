package com.jasbir.movieapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Jasbir Singh
 */

public class FavoriteContract {
    public static final String AUTHORITY = "com.environer.popularmovie";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVORITE_LIST = "Favorite_list";

    public static class FavoriteEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_LIST).build();
        public static final String TABLE_NAME = "Favorite_list";
        public static final String MOVIENAME_COLUMN = "Movie_Name";
        public static final String RELEASEDATE_COLUMN = "Release_Date"
                , RATING_COLUMN = "Length", FAVORITE_CHECKER_COLUMN="Favorite_or_not"
                , OVERVIEW_COLUMN = "Overview", IMAGESTORAGELOCATION_COLUMN="Image_Location";


    }
}
