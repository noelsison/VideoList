package com.example.noel.videolist.video;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.noel.videolist.R;
import com.example.noel.videolist.data.VideoListContentProvider;
import com.example.noel.videolist.data.VideoListContract;

import com.example.noel.videolist.data.VideoListContract.MediaItemEntry;

public class VideoPlayerActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DB_LOADER = 0;

    public static final String INTENT_EXTRA_ID = "CONTENT_ID";
    private static final String STATE_VIDEO_POS = "VIDEO_POS";
    private static final String STATE_VIDEO_PLAYING = "VIDEO_PLAYING";

    String mTitle;
    int mediaItemId;
    int videoPosition;
    boolean videoWasPlaying;

    VideoView videoView;
    MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        mediaItemId = this.getIntent().getIntExtra(INTENT_EXTRA_ID, 0);
        if (savedInstanceState != null) {
            videoPosition = savedInstanceState.getInt(STATE_VIDEO_POS);
            videoWasPlaying = savedInstanceState.getBoolean(STATE_VIDEO_PLAYING);
        } else {
            videoPosition = 0;
            videoWasPlaying = true; // Autoplay video by default
        }

        mediaController = new MediaController(this);
        videoView = (VideoView) findViewById(R.id.vv_player);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        getLoaderManager().initLoader(DB_LOADER, null, this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (videoView.getCurrentPosition() > 0) {
            outState.putInt(STATE_VIDEO_POS, videoView.getCurrentPosition());
            outState.putBoolean(STATE_VIDEO_PLAYING, videoView.isPlaying());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.video_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_full_screen_video_player:
                Toast.makeText(this, "Fullscreen menu option clicked.", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public int getMediaItemId() {
        return mediaItemId;
    }

    public void playVideo(String filename) {
        Uri videoUri = Uri.parse("android.resource://" + getPackageName()
                + "/" + getResources().getIdentifier(filename, "raw", getPackageName()));

        Log.d(VideoPlayerActivity.class.getName(), videoUri.toString());

        videoView.setVideoURI(videoUri);
        videoView.requestFocus();
        videoView.seekTo(videoPosition);
        if (videoWasPlaying) {
            videoView.start();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case DB_LOADER:
                String mediaItemIdString = Integer.toString(mediaItemId);
                return new CursorLoader(this,
                        Uri.parse(VideoListContentProvider.MEDIA_URI + "/" + mediaItemIdString),
                        new String[] {MediaItemEntry.COLUMN_TITLE, MediaItemEntry.COLUMN_FILENAME},
                        MediaItemEntry._ID + " = ?",
                        new String[] {mediaItemIdString},
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
        getSupportActionBar().setTitle(title);
        playVideo(filename);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
