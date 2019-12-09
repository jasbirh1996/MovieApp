package com.jasbir.movieapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jasbir Singh
 */

public class FavoriteDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "fvr6.db";
    public static final int DATABASE_VERSION = 1;

    public FavoriteDbHelper(Context context){
        super(context,DATABASE_NAME, null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TABLE = "CREATE TABLE " + FavoriteContract.FavoriteEntry.TABLE_NAME + "(" + FavoriteContract.FavoriteEntry._ID
                +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + FavoriteContract.FavoriteEntry.MOVIENAME_COLUMN + " TEXT NOT NULL, "
                + FavoriteContract.FavoriteEntry.IMAGESTORAGELOCATION_COLUMN + " TEXT NOT NULL, "
                + FavoriteContract.FavoriteEntry.RATING_COLUMN + " TEXT NOT NULL, "
                + FavoriteContract.FavoriteEntry.OVERVIEW_COLUMN + " TEXT NOT NULL, "
                + FavoriteContract.FavoriteEntry.RELEASEDATE_COLUMN + " TEXT NOT NULL" + ");";

        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteContract.FavoriteEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
