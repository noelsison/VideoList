package com.example.noel.videolist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.noel.videolist.data.VideoListContract.ContentItemEntry;
import com.example.noel.videolist.data.VideoListContract.MediaItemEntry;

/**
 * Created by Noel on 2/13/2017.
 */

public class VideoListDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "videoList.db";
    public static final int DATABASE_VERSION = 1;

    private static VideoListDbHelper instance;

    private VideoListDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized VideoListDbHelper getInstance(Context context) {
        if(instance == null) {
            instance = new VideoListDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // ContentItem Table
        final String SQL_CREATE_CONTENT_ITEM_TABLE = "CREATE TABLE " + ContentItemEntry.TABLE_NAME + " (" +
                ContentItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContentItemEntry.COLUMN_TYPE + " INTEGER NOT NULL, " +
                ContentItemEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                ContentItemEntry.COLUMN_CONTENT_ID + " INETGER NOT NULL" +
                " ); ";
        Log.d(VideoListDbHelper.class.getName(), SQL_CREATE_CONTENT_ITEM_TABLE);
        db.execSQL(SQL_CREATE_CONTENT_ITEM_TABLE);

        // MediaItem Table
        final String SQL_CREATE_MEDIA_ITEM_TABLE = "CREATE TABLE " + MediaItemEntry.TABLE_NAME + " (" +
                MediaItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MediaItemEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MediaItemEntry.COLUMN_FILENAME + " TEXT NOT NULL" +
                " ); ";
        Log.d(VideoListDbHelper.class.getName(), SQL_CREATE_MEDIA_ITEM_TABLE);
        db.execSQL(SQL_CREATE_MEDIA_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: Change to use proper migration
        db.execSQL("DROP TABLE IF EXISTS " + ContentItemEntry.TABLE_NAME + ";");
        db.execSQL("DROP TABLE IF EXISTS " + MediaItemEntry.TABLE_NAME + ";");
        onCreate(db);
    }
}
