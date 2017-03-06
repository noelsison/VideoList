package com.example.noel.videolist.content;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import com.example.noel.videolist.data.VideoListContentProvider;
import com.example.noel.videolist.data.VideoListContract.ContentItemEntry;

/**
 * Created by Noel on 3/6/2017.
 */

public class ContentListLoadManager  implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DB_LOADER = 0;

    ContentListActivity activity;

    SQLiteDatabase db;

    ContentListLoadManager(ContentListActivity activity) {
        this.activity = activity;
        this.activity.getLoaderManager().initLoader(DB_LOADER, null, this);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case DB_LOADER:
                String moduleId = Integer.toString(activity.getModuleId());
                return new CursorLoader(activity,
                        Uri.parse(VideoListContentProvider.MODULE_URI + "/" + moduleId),
                        new String[]{
                                ContentItemEntry.COLUMN_TYPE,
                                ContentItemEntry.COLUMN_TITLE,
                                ContentItemEntry.COLUMN_CONTENT_ID
                        },
                        ContentItemEntry.COLUM_MODULE_ID + " = ?",
                        new String[] {moduleId},
                        ContentItemEntry.COLUMN_SEQ_NUM);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        activity.getAdapter().swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        activity.getAdapter().swapCursor(null);
    }
}
