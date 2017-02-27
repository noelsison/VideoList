package com.example.noel.videolist.video;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.noel.videolist.MainActivity;
import com.example.noel.videolist.data.TestUtil;
import com.example.noel.videolist.data.VideoListContract;
import com.example.noel.videolist.data.VideoListDbHelper;


import com.example.noel.videolist.data.VideoListContract.MediaItemEntry;

/**
 * Created by Noel on 2/27/2017.
 */

public class VideoPlayerLoadManager implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DB_LOADER = 0;

    VideoPlayerActivity activity;

    SQLiteDatabase mDb;

    public VideoPlayerLoadManager(VideoPlayerActivity activity) {
        this.activity = activity;
        this.activity.getLoaderManager().initLoader(DB_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case DB_LOADER:
                return new CursorLoader(activity,
                        null,
                        new String[] {MediaItemEntry.COLUMN_TITLE, MediaItemEntry.COLUMN_FILENAME},
                        MediaItemEntry._ID + " = ?",
                        new String[] {Integer.toString(activity.getCurrentMediaItemId())},
                        VideoListContract.MediaItemEntry.COLUMN_TITLE) {
                    @Override
                    public Cursor loadInBackground() {
                        VideoListDbHelper dbHelper = VideoListDbHelper.getInstance(activity);
                        mDb = dbHelper.getWritableDatabase();
                        // Insert test data to DB
                        TestUtil.insertFakeData(mDb);

                        return mDb.query(MediaItemEntry.TABLE_NAME,
                                getProjection(),
                                getSelection(),
                                getSelectionArgs(),
                                null,
                                null,
                                getSortOrder());
                    }
                };
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        String title = data.getString(data.getColumnIndex(MediaItemEntry.COLUMN_TITLE));
        String filename = data.getString(data.getColumnIndex(MediaItemEntry.COLUMN_FILENAME));
        activity.getSupportActionBar().setTitle(title);
        activity.playVideo(filename);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
