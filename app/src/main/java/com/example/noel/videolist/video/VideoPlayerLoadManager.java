package com.example.noel.videolist.video;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import com.example.noel.videolist.MainActivity;
import com.example.noel.videolist.data.TestUtil;
import com.example.noel.videolist.data.VideoListContentProvider;
import com.example.noel.videolist.data.VideoListContract;
import com.example.noel.videolist.data.VideoListDbHelper;


import com.example.noel.videolist.data.VideoListContract.MediaItemEntry;

/**
 * Created by Noel on 2/27/2017.
 */

public class VideoPlayerLoadManager implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DB_LOADER = 0;

    VideoPlayerActivity activity;

    SQLiteDatabase db;

    public VideoPlayerLoadManager(VideoPlayerActivity activity) {
        this.activity = activity;
        this.activity.getLoaderManager().initLoader(DB_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case DB_LOADER:
                String mediaItemId = Integer.toString(activity.getCurrentMediaItemId());
                return new CursorLoader(activity,
                        Uri.parse(VideoListContentProvider.MEDIA_URI + "/" + mediaItemId),
                        new String[] {MediaItemEntry.COLUMN_TITLE, MediaItemEntry.COLUMN_FILENAME},
                        MediaItemEntry._ID + " = ?",
                        new String[] {mediaItemId},
                        VideoListContract.MediaItemEntry.COLUMN_TITLE);
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
