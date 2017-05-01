package com.example.noel.videolist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.noel.videolist.data.VideoListContract.ModuleEntry;
import com.example.noel.videolist.data.VideoListContract.TopicEntry;
import com.example.noel.videolist.data.VideoListContract.ContentEntry;
import com.example.noel.videolist.data.VideoListContract.MediaEntry;

/**
 * Created by Noel on 2/13/2017.
 */

public class VideoListDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "videoList.db";
    public static final int DATABASE_VERSION = 3;

    private static VideoListDbHelper instance;

    private VideoListDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized VideoListDbHelper getInstance(Context context) {
        if(instance == null) {
            instance = new VideoListDbHelper(context.getApplicationContext());
            // TODO: Remove inserting fake data when db has actual content
            TestUtil.insertFakeData(instance.getWritableDatabase());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Module Table
        final String SQL_CREATE_MODULE_TABLE = "CREATE TABLE " + ModuleEntry.TABLE_NAME + " (" +
                ModuleEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ModuleEntry.COLUMN_TITLE + " TEXT NOT NULL" +
                " ); ";
        Log.d(VideoListDbHelper.class.getName(), SQL_CREATE_MODULE_TABLE);
        db.execSQL(SQL_CREATE_MODULE_TABLE);

        // TopicEntry Table
        final String SQL_CREATE_TOPIC_ITEM_TABLE = "CREATE TABLE " + TopicEntry.TABLE_NAME + " (" +
                TopicEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TopicEntry.COLUMN_MODULE_ID + " INTEGER NOT NULL, " +
                TopicEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                TopicEntry.COLUMN_SEQ_NUM + " INTEGER NOT NULL" +
                " ); ";
        Log.d(VideoListDbHelper.class.getName(), SQL_CREATE_TOPIC_ITEM_TABLE);
        db.execSQL(SQL_CREATE_TOPIC_ITEM_TABLE);

        // ContentItem Table
        final String SQL_CREATE_CONTENT_ITEM_TABLE = "CREATE TABLE " + ContentEntry.TABLE_NAME + " (" +
                ContentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContentEntry.COLUMN_TOPIC_ID + " INTEGER NOT NULL, " +
                ContentEntry.COLUMN_TYPE + " INTEGER NOT NULL, " +
                ContentEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                ContentEntry.COLUMN_COVER_ART_PATH + " TEXT NOT NULL, " +
                ContentEntry.COLUMN_CONTENT_ID + " INTEGER NOT NULL," +
                ContentEntry.COLUMN_SEQ_NUM + " INTEGER NOT NULL" +
                " ); ";
        Log.d(VideoListDbHelper.class.getName(), SQL_CREATE_CONTENT_ITEM_TABLE);
        db.execSQL(SQL_CREATE_CONTENT_ITEM_TABLE);

        // MediaItem Table
        final String SQL_CREATE_MEDIA_ITEM_TABLE = "CREATE TABLE " + MediaEntry.TABLE_NAME + " (" +
                MediaEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MediaEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MediaEntry.COLUMN_FILENAME + " TEXT NOT NULL" +
                " ); ";
        Log.d(VideoListDbHelper.class.getName(), SQL_CREATE_MEDIA_ITEM_TABLE);
        db.execSQL(SQL_CREATE_MEDIA_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: Change to use proper migration
        db.execSQL("DROP TABLE IF EXISTS " + ModuleEntry.TABLE_NAME + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TopicEntry.TABLE_NAME + ";");
        db.execSQL("DROP TABLE IF EXISTS " + ContentEntry.TABLE_NAME + ";");
        db.execSQL("DROP TABLE IF EXISTS " + MediaEntry.TABLE_NAME + ";");
        onCreate(db);
    }
}
