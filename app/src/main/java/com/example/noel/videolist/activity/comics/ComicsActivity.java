package com.example.noel.videolist.activity.comics;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.noel.videolist.R;
import com.example.noel.videolist.data.VideoListContentProvider;
import com.example.noel.videolist.data.VideoListContract;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Noel on 4/10/2017.
 */

public class ComicsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String TAG = ComicsActivity.class.getName();

    private static final int DB_LOADER = 0;

    public static final String INTENT_EXTRA_ID = "CONTENT_ID";

    int mediaItemId;
    ProgressBar progressBar;
    ImageView imageView;
    BitmapAsyncLoader bitmapAsyncLoader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comics);

        mediaItemId = getIntent().getIntExtra(INTENT_EXTRA_ID, 0);

        progressBar = (ProgressBar) findViewById(R.id.comics_pb);
        progressBar.setIndeterminate(true);
        imageView = (ImageView) findViewById(R.id.comics_iv);

        bitmapAsyncLoader = new BitmapAsyncLoader();

        getLoaderManager().initLoader(DB_LOADER, null, this);
    }

    private void setComics(String title, String filename) {
        getSupportActionBar().setTitle(title);
        bitmapAsyncLoader.execute(filename);
    }

    /*
    Do not use this on the UI thread
     */
    private Bitmap getBitmap(String filename) {
        Bitmap bitmap = null;
        try {
            InputStream inputStream = getAssets().open(filename);
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();

        } catch (IOException e) {
            Log.e(TAG, "Failed to open image: " + filename);
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case DB_LOADER:
                String mediaItemIdString = Integer.toString(mediaItemId);
                return new CursorLoader(this,
                        Uri.parse(VideoListContentProvider.MEDIA_URI + "/" + mediaItemIdString),
                        new String[]{VideoListContract.MediaEntry.COLUMN_TITLE, VideoListContract.MediaEntry.COLUMN_FILENAME},
                        null, null,
                        VideoListContract.MediaEntry.COLUMN_TITLE);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        String title = data.getString(data.getColumnIndex(VideoListContract.MediaEntry.COLUMN_TITLE));
        String filename = data.getString(data.getColumnIndex(VideoListContract.MediaEntry.COLUMN_FILENAME));
        setComics(title, filename);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private class BitmapAsyncLoader extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            String filename = params[0];
            Bitmap bitmap = null;
            try {
                InputStream inputStream = getAssets().open(filename);
                bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();

            } catch (IOException e) {
                Log.e(TAG, "Failed to open image: " + filename);
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.d(TAG, "Progress: " + values[0]);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            LinearLayout progressBarHolder = (LinearLayout) findViewById(R.id.comics_rl_progress_bar_holder);
            progressBarHolder.setVisibility(View.GONE);
            imageView.setImageBitmap(bitmap);
        }
    }
}
