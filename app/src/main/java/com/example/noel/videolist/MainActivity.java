package com.example.noel.videolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noel.videolist.content.ContentListActivity;

public class MainActivity extends AppCompatActivity implements ModuleListAdapter.ActivityListAdapterClickHandler {

    private final String TAG = MainActivity.class.getName();

    TextView textViewGreeting;
    RecyclerView recyclerView;
    ModuleListAdapter activityListAdapter;
    ModuleListLoadManager loadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Main UI
        setContentView(R.layout.activity_main);

        // Body UI
        textViewGreeting = (TextView) findViewById(R.id.tv_greeting);
        recyclerView = (RecyclerView) findViewById(R.id.rv_activity_main);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Adapter that will connect the UI and DB fetch results
        activityListAdapter = new ModuleListAdapter(this, null);
        recyclerView.setAdapter(activityListAdapter);

        // Handles DB
        loadManager = new ModuleListLoadManager(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                break;
            case R.id.action_assessment:
                Toast.makeText(this, "Assessment menu option clicked.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_profile:
                Toast.makeText(this, "Profile menu option clicked.", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int moduleId, String title) {
        Intent intent = new Intent(getApplicationContext(), ContentListActivity.class);
        intent.putExtra(ContentListActivity.INTENT_EXTRA_MODULE_ID, moduleId);
        intent.putExtra(ContentListActivity.INTENT_EXTRA_MODULE_TITLE, title);
        startActivity(intent);
    }

    public ModuleListAdapter getActivityListAdapter() {
        return activityListAdapter;
    }

    public TextView getTextViewGreeting() {
        return textViewGreeting;
    }

}
