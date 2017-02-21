package com.example.noel.videolist;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.noel.videolist.data.TestUtil;
import com.example.noel.videolist.data.VideoListDbHelper;

import com.example.noel.videolist.data.DbConstants.ContentType;
import com.example.noel.videolist.data.VideoListContract.ContentItemEntry;
import com.example.noel.videolist.data.VideoListContract.MediaItemEntry;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ActivityListAdapter.ActivityListAdapterClickHandler {

    public final String TAG = MainActivity.class.getName();

    RecyclerView mRecycerView;
    ActivityListAdapter mActivityListAdapter;

    SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Main UI
        setContentView(R.layout.activity_main);

        // Body UI
        mRecycerView = (RecyclerView) findViewById(R.id.rv_activity_main);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecycerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecycerView.getContext(),
                layoutManager.getOrientation());
        mRecycerView.addItemDecoration(dividerItemDecoration);

        // DB
        VideoListDbHelper dbHelper = new VideoListDbHelper(this);
        mDb = dbHelper.getWritableDatabase();
        // Insert test data to DB
        TestUtil.insertFakeData(mDb);

        // Fetch ContentItem objects to display
        Cursor cursor = getAllContentItems();

        // Adapter that will connect the UI and DB fetch results
        mActivityListAdapter = new ActivityListAdapter(this, cursor);
        mRecycerView.setAdapter(mActivityListAdapter);
    }

    private Cursor getAllContentItems() {
        return mDb.query(ContentItemEntry.TABLE_NAME,
                new String[]{ContentItemEntry.COLUMN_TYPE, ContentItemEntry.COLUMN_TITLE, ContentItemEntry.COLUMN_CONTENT_ID},
                null,
                null,
                null,
                null,
                MediaItemEntry.COLUMN_TITLE);
    }

    @Override
    public void onClick(int type, int contentId) {
        switch (type) {
            case ContentType.VIDEO:
                Intent intent = new Intent(getApplicationContext(), VideoPlayerActivity.class);
                intent.putExtra("ID", contentId);
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
}
