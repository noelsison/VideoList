package com.example.noel.videolist.content;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.noel.videolist.R;
import com.example.noel.videolist.data.DbConstants;
import com.example.noel.videolist.data.DbConstants.ContentType;
import com.example.noel.videolist.video.VideoPlayerActivity;

/**
 * Created by Noel on 3/6/2017.
 */

public class ContentListActivity  extends AppCompatActivity implements ContentListAdapter.ActivityListAdapterClickHandler {

    public static final String INTENT_EXTRA_MODULE_ID = "MODULE_ID";
    public static final String INTENT_EXTRA_MODULE_TITLE = "MODULE_TITLE";
    private final String TAG = ContentListActivity.class.getName();

    RecyclerView recyclerView;
    ContentListAdapter adapter;
    ContentListLoadManager loadManager;

    int moduleId;
    String moduleTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        moduleId = this.getIntent().getIntExtra(INTENT_EXTRA_MODULE_ID, 0);
        moduleTitle = this.getIntent().getStringExtra(INTENT_EXTRA_MODULE_TITLE);

        // Main UI
        setContentView(R.layout.content_list_activity);
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
        loadManager = new ContentListLoadManager(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onItemClick(int type, int contentId) {
        switch (type) {
            case ContentType.VIDEO:
                Intent intent = new Intent(getApplicationContext(), VideoPlayerActivity.class);
                intent.putExtra(VideoPlayerActivity.INTENT_EXTRA_ID, contentId);
                startActivity(intent);
                break;
            case ContentType.AUDIO_RECORD:
            default:
                Toast.makeText(this, String.format("Content contentId %d of contentType %s not supported",
                        contentId, DbConstants.ContentType.toString(type)), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public ContentListAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(ContentListAdapter adapter) {
        this.adapter = adapter;
    }

    public int getModuleId() {
        return moduleId;
    }
}
