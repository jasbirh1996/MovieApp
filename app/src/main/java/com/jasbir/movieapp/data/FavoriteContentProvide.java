package com.jasbir.movieapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Jasbir Singh
 */

public class FavoriteContentProvide extends ContentProvider {
    public static final int FAVORITELIST = 100;
    public static final int FAVORITE_LIST_WITH_TITLE = 101 ;

    public static final UriMatcher sUriMathcher = BuildUriMatcher();
    public static UriMatcher BuildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(FavoriteContract.AUTHORITY,FavoriteContract.PATH_FAVORITE_LIST,FAVORITELIST);
        uriMatcher.addURI(FavoriteContract.AUTHORITY,FavoriteContract.PATH_FAVORITE_LIST + "/*",FAVORITE_LIST_WITH_TITLE);
    return uriMatcher;
    }
    FavoriteDbHelper favoriteDbHelper;
    @Override
    public boolean onCreate() {
        Context context = getContext();
        favoriteDbHelper = new FavoriteDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projections, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = favoriteDbHelper.getReadableDatabase();

        int match = sUriMathcher.match(uri);
        Cursor retCursor=null;

        switch (match){
//            case FAVORITE_LIST_WITH_TITLE:
//                String title = uri.getPathSegments().get(1);
//
//                String mSelection = FavoriteContract.FavoriteEntry.MOVIENAME_COLUMN+"=?";
//                String[] mSelectionArgs = new String[]{title};
//
//                retCursor = db.query(FavoriteContract.FavoriteEntry.TABLE_NAME,
//                        strings,mSelection,mSelectionArgs,null,null,null);
//                break;
            case FAVORITELIST:
                retCursor = db.query(FavoriteContract.FavoriteEntry.TABLE_NAME,
                        projections,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Uknown Uri"+ uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = favoriteDbHelper.getWritableDatabase();
        int match = sUriMathcher.match(uri);

        Uri returnUri=null;
        switch(match){
            case FAVORITELIST:
                long id = db.insert(FavoriteContract.FavoriteEntry.TABLE_NAME,null,contentValues);
                if(id>0){
                    returnUri = ContentUris.withAppendedId(FavoriteContract.FavoriteEntry.CONTENT_URI,id);
                }
                else{
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: "+ uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String whereClause, @Nullable String[] whereArgs) {
        SQLiteDatabase db = favoriteDbHelper.getWritableDatabase();
        int match = sUriMathcher.match(uri);
        int n = 0;
        switch (match){
            case FAVORITELIST:
                n= db.delete(FavoriteContract.FavoriteEntry.TABLE_NAME
                ,whereClause,whereArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        return n;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
