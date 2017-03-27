package com.example.noel.videolist.activity.content;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.noel.videolist.R;
import com.example.noel.videolist.activity.audio.AudioRecorderActivity;
import com.example.noel.videolist.data.DbConstants;
import com.example.noel.videolist.data.DbConstants.ContentType;
import com.example.noel.videolist.data.VideoListContentProvider;
import com.example.noel.videolist.data.VideoListContract;
import com.example.noel.videolist.activity.video.VideoPlayerActivity;

/**
 * Created by Noel on 3/6/2017.
 */

public class ContentListActivity  extends AppCompatActivity implements ContentListAdapter.ActivityListAdapterClickHandler, LoaderManager.LoaderCallbacks<Cursor> {

    private final String TAG = ContentListActivity.class.getName();
    private static final int DB_LOADER = 0;

    public static final String INTENT_EXTRA_MODULE_ID = "MODULE_ID";
    public static final String INTENT_EXTRA_MODULE_TITLE = "MODULE_TITLE";

    RecyclerView recyclerView;
    ContentListAdapter adapter;

    int moduleId;
    String moduleTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        moduleId = this.getIntent().getIntExtra(INTENT_EXTRA_MODULE_ID, 0);
        moduleTitle = this.getIntent().getStringExtra(INTENT_EXTRA_MODULE_TITLE);

        // Main UI
        setContentView(R.layout.activity_content_list);
        getSupportActionBar().setTitle(moduleTitle);

        // Body UI
        recyclerView = (RecyclerView) findViewById(R.id.rv_content_list);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Adapter that will connect the UI and DB fetch results
        adapter = new ContentListAdapter(this, null);
        recyclerView.setAdapter(adapter);

        // Handles DB
        getLoaderManager().initLoader(DB_LOADER, null, this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onItemClick(int type, int contentId) {
        Intent intent;
        switch (type) {
            case ContentType.VIDEO:
                intent = new Intent(getApplicationContext(), VideoPlayerActivity.class);
                intent.putExtra(VideoPlayerActivity.INTENT_EXTRA_ID, contentId);
                startActivity(intent);
                break;
            case ContentType.AUDIO_RECORD:
                intent =  new Intent(getApplicationContext(), AudioRecorderActivity.class);
                startActivity(intent);
                break;
            default:
                Toast.makeText(this, String.format("Content contentId %d of contentType %s not supported",
                        contentId, DbConstants.ContentType.toString(type)), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case DB_LOADER:
                String moduleIdString = Integer.toString(moduleId);
                return new CursorLoader(this,
                        Uri.parse(VideoListContentProvider.MODULE_URI + "/" + moduleIdString),
                        new String[]{
                                VideoListContract.ContentItemEntry.COLUMN_TYPE,
                                VideoListContract.ContentItemEntry.COLUMN_TITLE,
                                VideoListContract.ContentItemEntry.COLUMN_CONTENT_ID
                        },
                        VideoListContract.ContentItemEntry.COLUMN_MODULE_ID + " = ?",
                        new String[] {moduleIdString},
                        VideoListContract.ContentItemEntry.COLUMN_SEQ_NUM);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
