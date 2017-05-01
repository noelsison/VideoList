package com.example.noel.videolist.activity.list;

import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.noel.videolist.R;
import com.example.noel.videolist.activity.base.BaseRecyclerListActivity;
import com.example.noel.videolist.activity.comics.ComicsActivity;
import com.example.noel.videolist.activity.content.ContentListActivity;
import com.example.noel.videolist.activity.interview.practice.InterviewPracticeActivity;
import com.example.noel.videolist.activity.video.VideoPlayerActivity;
import com.example.noel.videolist.data.DbConstants;
import com.example.noel.videolist.data.VideoListContentProvider;
import com.example.noel.videolist.data.VideoListContract;

/**
 * Created by Noel on 4/23/2017.
 */

public class NestedListActivity extends BaseRecyclerListActivity {

    private final String TAG = ContentListActivity.class.getName();
    static final int TOPIC_LOADER = 0;
    static final int CONTENT_LOADER = 1;

    public static final String INTENT_EXTRA_MODULE_ID = "MODULE_ID";
    public static final String INTENT_EXTRA_MODULE_TITLE = "MODULE_TITLE";
    public static final String BUNDLE_MODULE_ID = "MODULE_ID";

    RecyclerView recyclerView;
    NestedListAdapter adapter;

    int moduleId;
    String moduleTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        moduleId = this.getIntent().getIntExtra(INTENT_EXTRA_MODULE_ID, 0);
        moduleTitle = this.getIntent().getStringExtra(INTENT_EXTRA_MODULE_TITLE);

        // Main UI
        setContentView(R.layout.activity_nested_list);
        getSupportActionBar().setTitle(moduleTitle);

        // Body UI
        recyclerView = (RecyclerView) findViewById(R.id.activity_nested_list_rv);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Adapter that will connect the UI and DB fetch results
        adapter = new NestedListAdapter(this);
        recyclerView.setAdapter(adapter);

        // Handles DB
        Bundle bundle = new Bundle(1);
        bundle.putString(BUNDLE_MODULE_ID, Integer.toString(moduleId));
        getLoaderManager().initLoader(TOPIC_LOADER, bundle, this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onItemClick(VideoListContract.Model model) {
        VideoListContract.ContentEntry contentItemEntry = (VideoListContract.ContentEntry) model;
        Intent intent;
        switch (contentItemEntry.getType()) {
            case DbConstants.ContentType.VIDEO:
                intent = new Intent(getApplicationContext(), VideoPlayerActivity.class);
                intent.putExtra(VideoPlayerActivity.INTENT_EXTRA_ID, contentItemEntry.getContentId());
                startActivity(intent);
                break;
            case DbConstants.ContentType.AUDIO_RECORD:
                intent = new Intent(getApplicationContext(), InterviewPracticeActivity.class);
                intent.putExtra(InterviewPracticeActivity.INTENT_EXTRA_CONTENT_ID, contentItemEntry.getId());
                intent.putExtra(InterviewPracticeActivity.INTENT_EXTRA_CONTENT_TITLE, contentItemEntry.getTitle());
                startActivity(intent);
                break;
            case DbConstants.ContentType.IMAGE:
                intent = new Intent(getApplicationContext(), ComicsActivity.class);
                intent.putExtra(ComicsActivity.INTENT_EXTRA_ID, contentItemEntry.getContentId());
                startActivity(intent);
                break;
            default:
                Toast.makeText(this, String.format("Content contentId %d of contentType %s not supported",
                        contentItemEntry.getContentId(), DbConstants.ContentType.toString(contentItemEntry.getType())), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case TOPIC_LOADER:
                return new CursorLoader(this,
                        Uri.parse(VideoListContentProvider.MODULE_URI + "/" + args.getString(BUNDLE_MODULE_ID)),
                        null, null, null,
                        VideoListContract.TopicEntry.COLUMN_SEQ_NUM);
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