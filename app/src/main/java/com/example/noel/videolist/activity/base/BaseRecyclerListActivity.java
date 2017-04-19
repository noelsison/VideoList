package com.example.noel.videolist.activity.base;

import android.app.LoaderManager;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
/**
 * Created by Noel on 3/27/2017.
 */

public abstract class BaseRecyclerListActivity extends AppCompatActivity implements BaseRecyclerListAdapter.ActivityListAdapterClickHandler, LoaderManager.LoaderCallbacks<Cursor> {

}
