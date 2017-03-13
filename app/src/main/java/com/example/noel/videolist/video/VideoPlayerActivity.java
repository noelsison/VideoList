package com.example.noel.videolist.video;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
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
    public static final String INTENT_VIDEO_POS = "VIDEO_POS";
    public static final String INTENT_VIDEO_PLAYING = "VIDEO_PLAYING";
    private static final String STATE_FULLSCREEN = "FULLSCREEN";

    private final String FULLSCREEN_PROMPT = "Press 'back' to exit fullscreen";

    int mediaItemId;
    int videoPosition;
    boolean videoWasPlaying;
    boolean isFullScreen;

    LinearLayout videoHolder;
    VideoView videoView;
    MediaController mediaController;

    LinearLayout.LayoutParams defaultScreenParams;
    LinearLayout.LayoutParams fullScreenParams;
    Runnable fullScreenRunnable;
    Runnable defaultScreenRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        getSupportActionBar().setShowHideAnimationEnabled(false);

        mediaItemId = getIntent().getIntExtra(INTENT_EXTRA_ID, 0);
        videoHolder = (LinearLayout) findViewById(R.id.ll_video_player_holder);
        mediaController = new MediaController(this);
        videoView = (VideoView) findViewById(R.id.vv_video_player);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        initLayoutChanger();

        if (savedInstanceState != null) {
            videoPosition = savedInstanceState.getInt(INTENT_VIDEO_POS);
            videoWasPlaying = savedInstanceState.getBoolean(INTENT_VIDEO_PLAYING);
            isFullScreen = savedInstanceState.getBoolean(STATE_FULLSCREEN);
            setFullscreenMode(isFullScreen);
        } else {
            videoPosition = getIntent().getIntExtra(INTENT_VIDEO_POS, 0);
            // Auto-play video by default
            videoWasPlaying = getIntent().getBooleanExtra(INTENT_VIDEO_PLAYING, true);
        }
        getLoaderManager().initLoader(DB_LOADER, null, this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setFullscreenMode(true);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setFullscreenMode(false);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_FULLSCREEN, isFullScreen);
        if (videoView.getCurrentPosition() > 0) {
            outState.putInt(INTENT_VIDEO_POS, videoView.getCurrentPosition());
            outState.putBoolean(INTENT_VIDEO_PLAYING, videoView.isPlaying());
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
                setFullscreenMode(true);
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (isFullScreen) {
            setFullscreenMode(false);
        } else {
            super.onBackPressed();
        }
    }

    private void initLayoutChanger() {
        defaultScreenParams = (LinearLayout.LayoutParams) videoHolder.getLayoutParams();
        fullScreenParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        fullScreenParams.gravity = Gravity.CENTER;

        final View decorView = getWindow().getDecorView();

        defaultScreenRunnable = new Runnable() {
            @Override
            public void run() {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getSupportActionBar().show();
                videoHolder.setLayoutParams(defaultScreenParams);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            }
        };
        fullScreenRunnable = new Runnable() {
            @Override
            public void run() {
                getSupportActionBar().hide();
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                videoHolder.setLayoutParams(fullScreenParams);
                if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                    Toast.makeText(getApplicationContext(), FULLSCREEN_PROMPT, Toast.LENGTH_SHORT).show();
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
            }
        };
    }

    public void setFullscreenMode(boolean isFullScreen) {
        if (this.isFullScreen == isFullScreen) {
            return;
        } else {
            this.isFullScreen = isFullScreen;
            if (isFullScreen) {
                this.runOnUiThread(fullScreenRunnable);
            } else {
                this.runOnUiThread(defaultScreenRunnable);
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case DB_LOADER:
                String mediaItemIdString = Integer.toString(mediaItemId);
                return new CursorLoader(this,
                        Uri.parse(VideoListContentProvider.MEDIA_URI + "/" + mediaItemIdString),
                        new String[]{MediaItemEntry.COLUMN_TITLE, MediaItemEntry.COLUMN_FILENAME},
                        MediaItemEntry._ID + " = ?",
                        new String[]{mediaItemIdString},
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

    protected void playVideo(String filename) {
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
