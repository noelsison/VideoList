package com.example.noel.videolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.noel.videolist.data.DbConstants.ContentType;

public class MainActivity extends AppCompatActivity implements ActivityListAdapter.ActivityListAdapterClickHandler {

    public final String TAG = MainActivity.class.getName();

    RecyclerView recyclerView;
    ActivityListAdapter activityListAdapter;
    MainActivityController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Main UI
        setContentView(R.layout.activity_main);

        // Body UI
        recyclerView = (RecyclerView) findViewById(R.id.rv_activity_main);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Adapter that will connect the UI and DB fetch results
        activityListAdapter = new ActivityListAdapter(this, null);
        recyclerView.setAdapter(activityListAdapter);

        // Handles DB
        controller = new MainActivityController(this);
    }

    @Override
    public void onClick(int type, int contentId) {
        switch (type) {
            case ContentType.VIDEO:
                Intent intent = new Intent(getApplicationContext(), VideoPlayerActivity.class);
                intent.putExtra(VideoPlayerActivity.INTENT_EXTRA_ID, contentId);
                startActivity(intent);
                break;
            case ContentType.AUDIO_RECORD:
                break;
            default:
                Toast.makeText(this, String.format("Content contentId %d of contentType %d not supported",
                        contentId, type), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public ActivityListAdapter getActivityListAdapter() {
        return activityListAdapter;
    }

    public void setActivityListAdapter(ActivityListAdapter activityListAdapter) {
        this.activityListAdapter = activityListAdapter;
    }
}
