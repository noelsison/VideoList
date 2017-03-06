package com.example.noel.videolist.video;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.noel.videolist.R;
import com.example.noel.videolist.data.VideoListDbHelper;

import com.example.noel.videolist.data.VideoListContract.MediaItemEntry;

public class VideoPlayerActivity extends AppCompatActivity {

    public static final String INTENT_EXTRA_ID = "CONTENT_ID";
    private static final String STATE_VIDEO_POS = "VIDEO_POS";
    private static final String STATE_VIDEO_PLAYING = "VIDEO_PLAYING";

    String mTitle;
    int currentMediaItemId;
    int videoPosition;
    boolean videoWasPlaying;

    VideoView videoView;
    MediaController mediaController;
    VideoPlayerLoadManager loadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        currentMediaItemId = this.getIntent().getIntExtra(INTENT_EXTRA_ID, 0);
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

        loadManager = new VideoPlayerLoadManager(this);

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

    public int getCurrentMediaItemId() {
        return currentMediaItemId;
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
}
