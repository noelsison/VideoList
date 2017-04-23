package com.example.noel.videolist.activity.content;

import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.noel.videolist.R;
import com.example.noel.videolist.activity.audio.AudioRecorderActivity;
import com.example.noel.videolist.activity.base.BaseRecyclerListActivity;
import com.example.noel.videolist.activity.comics.ComicsActivity;
import com.example.noel.videolist.activity.interview.practice.InterviewPracticeActivity;
import com.example.noel.videolist.data.DbConstants;
import com.example.noel.videolist.data.DbConstants.ContentType;
import com.example.noel.videolist.data.VideoListContentProvider;
import com.example.noel.videolist.data.VideoListContract.Model;
import com.example.noel.videolist.data.VideoListContract.ContentEntry;
import com.example.noel.videolist.activity.video.VideoPlayerActivity;

/**
 * Created by Noel on 3/6/2017.
 */

public class ContentListActivity extends BaseRecyclerListActivity {

    private final String TAG = ContentListActivity.class.getName();
    private static final int DB_LOADER = 0;

    public static final String INTENT_EXTRA_TOPIC_ID = "TOPIC_ID";
    public static final String INTENT_EXTRA_TOPIC_TITLE = "TOPIC_TITLE";

    RecyclerView recyclerView;
    ContentListAdapter adapter;

    int topicId;
    String topicTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        topicId = this.getIntent().getIntExtra(INTENT_EXTRA_TOPIC_ID, 0);
        topicTitle = this.getIntent().getStringExtra(INTENT_EXTRA_TOPIC_TITLE);

        // Main UI
        setContentView(R.layout.activity_content_list);
        getSupportActionBar().setTitle(topicTitle);

        // Body UI
        recyclerView = (RecyclerView) findViewById(R.id.rv_content_list);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Adapter that will connect the UI and DB fetch results
        adapter = new ContentListAdapter(this);
        recyclerView.setAdapter(adapter);

        // Handles DB
        getLoaderManager().initLoader(DB_LOADER, null, this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onItemClick(Model model) {
        ContentEntry contentItemEntry = (ContentEntry) model;
        Intent intent;
        switch (contentItemEntry.getType()) {
            case ContentType.VIDEO:
                intent = new Intent(getApplicationContext(), VideoPlayerActivity.class);
                intent.putExtra(VideoPlayerActivity.INTENT_EXTRA_ID, contentItemEntry.getContentId());
                startActivity(intent);
                break;
            case ContentType.AUDIO_RECORD:
                intent = new Intent(getApplicationContext(), InterviewPracticeActivity.class);
                startActivity(intent);
                break;
            case ContentType.IMAGE:
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
            case DB_LOADER:
                String moduleIdString = Integer.toString(topicId);
                return new CursorLoader(this,
                        Uri.parse(VideoListContentProvider.TOPIC_URI + "/" + moduleIdString),
                        null, null, null,
                        ContentEntry.COLUMN_SEQ_NUM);
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
