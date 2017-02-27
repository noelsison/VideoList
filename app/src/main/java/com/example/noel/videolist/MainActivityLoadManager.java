package com.example.noel.videolist;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;


import com.example.noel.videolist.data.TestUtil;
import com.example.noel.videolist.data.VideoListContentProvider;
import com.example.noel.videolist.data.VideoListContract.ContentItemEntry;
import com.example.noel.videolist.data.VideoListContract.MediaItemEntry;
import com.example.noel.videolist.data.VideoListDbHelper;

/**
 * Created by Noel on 2/27/2017.
 */

public class MainActivityLoadManager implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DB_LOADER = 0;

    MainActivity activity;

    SQLiteDatabase db;

    MainActivityLoadManager(MainActivity activity) {
        this.activity = activity;
        this.activity.getLoaderManager().initLoader(DB_LOADER, null, this);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case DB_LOADER:
                return new CursorLoader(activity,
                        VideoListContentProvider.CONTENT_URI,
                        new String[]{
                                ContentItemEntry.COLUMN_TYPE,
                                ContentItemEntry.COLUMN_TITLE,
                                ContentItemEntry.COLUMN_CONTENT_ID
                        },
                        null,
                        null,
                        MediaItemEntry.COLUMN_TITLE);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        activity.getActivityListAdapter().swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        activity.getActivityListAdapter().swapCursor(null);
    }
}
