package com.example.noel.videolist;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.noel.videolist.data.TestUtil;
import com.example.noel.videolist.data.VideoListDbHelper;

import com.example.noel.videolist.data.VideoListContract.MediaItemEntry;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView mVideoListView;

    SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVideoListView = (ListView) findViewById(R.id.lv_videos);

        // DB
        VideoListDbHelper dbHelper = new VideoListDbHelper(this);
        mDb = dbHelper.getWritableDatabase();

        TestUtil.insertFakeData(mDb);

        String[] videoTitles = this.getAllVideoTitles();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, videoTitles);

        mVideoListView.setAdapter(adapter);
        mVideoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) mVideoListView.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), VideoPlayerActivity.class);
                intent.putExtra("VIDEO_TITLE", item);
                startActivity(intent);
            }
        });
    }

    private String[] getAllVideoTitles() {
        List<String> titles = new ArrayList<String>();
        Cursor cursor = mDb.query(MediaItemEntry.TABLE_NAME,
                new String[] {MediaItemEntry.COLUMN_TITLE},
                null,
                null,
                null,
                null,
                MediaItemEntry.COLUMN_TITLE);

        while(cursor.moveToNext()) {
            titles.add(cursor.getString(cursor.getColumnIndex(MediaItemEntry.COLUMN_TITLE)));
        }

        return titles.toArray(new String[titles.size()]);
    }
}
