package com.example.noel.videolist;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import com.example.noel.videolist.data.TestUtil;
import com.example.noel.videolist.data.VideoListContract.ContentItemEntry;
import com.example.noel.videolist.data.VideoListContract.MediaItemEntry;
import com.example.noel.videolist.data.VideoListDbHelper;

/**
 * Created by Noel on 2/27/2017.
 */

public class MainActivityController implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DB_LOADER = 0;

    MainActivity mActivity;

    SQLiteDatabase mDb;

    MainActivityController(MainActivity activity) {
        this.mActivity = activity;
        mActivity.getLoaderManager().initLoader(DB_LOADER, null, this);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case DB_LOADER:
                return new CursorLoader(mActivity,
                        null,
                        new String[]{ContentItemEntry.COLUMN_TYPE, ContentItemEntry.COLUMN_TITLE, ContentItemEntry.COLUMN_CONTENT_ID},
                        null,
                        null,
                        MediaItemEntry.COLUMN_TITLE) {
                    @Override
                    public Cursor loadInBackground() {
                        // DB
                        VideoListDbHelper dbHelper = new VideoListDbHelper(mActivity);
                        mDb = dbHelper.getWritableDatabase();
                        // Insert test data to DB
                        TestUtil.insertFakeData(mDb);

                        return mDb.query(ContentItemEntry.TABLE_NAME,
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
        mActivity.getActivityListAdapter().swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mActivity.getActivityListAdapter().swapCursor(null);
    }
}
