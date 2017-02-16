package com.example.noel.videolist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.noel.videolist.data.VideoListContract.MediaItemEntry;

/**
 * Created by Noel on 2/13/2017.
 */

public class VideoListDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "videoList.db";
    public static final int DATABASE_VERSION = 1;

    public VideoListDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
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
        db.execSQL("DROP TABLE IF EXISTS " + MediaItemEntry.TABLE_NAME + ";");
        onCreate(db);
    }
}
