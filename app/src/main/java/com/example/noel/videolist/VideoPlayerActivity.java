package com.example.noel.videolist;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.noel.videolist.data.VideoListContract;
import com.example.noel.videolist.data.VideoListDbHelper;

import java.util.ArrayList;
import java.util.List;

import com.example.noel.videolist.data.VideoListContract.MediaItemEntry;

public class VideoPlayerActivity extends AppCompatActivity {

    SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        // DB
        VideoListDbHelper dbHelper = new VideoListDbHelper(this);
        mDb = dbHelper.getReadableDatabase();

        String title = this.getIntent().getStringExtra("VIDEO_TITLE");
        String fileName = this.getFileName(title);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);

        VideoView videoView = (VideoView) findViewById(R.id.vv_player);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        Uri videoUri = Uri.parse("android.resource://" + getPackageName()
                + "/" + getResources().getIdentifier(fileName, "raw", getPackageName()));

        Log.d(VideoPlayerActivity.class.getName(), videoUri.toString());

        videoView.setMediaController(mediaController);
        videoView.setVideoURI(videoUri);
        videoView.requestFocus();
        videoView.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.create(this).
                            addNextIntentWithParentStack(upIntent).
                            startActivities();
                } else {
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String getFileName(String title) {
        String filename = null;
        Cursor cursor = mDb.query(MediaItemEntry.TABLE_NAME,
                new String[] {MediaItemEntry.COLUMN_FILENAME},
                MediaItemEntry.COLUMN_TITLE + " = ?",
                new String[] {title},
                null,
                null,
                null);

        if (cursor != null) {
            cursor.moveToFirst();
            filename = cursor.getString(cursor.getColumnIndex(MediaItemEntry.COLUMN_FILENAME));
        }

        return filename;
    }
}
