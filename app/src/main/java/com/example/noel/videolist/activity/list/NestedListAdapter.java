package com.example.noel.videolist.activity.list;

import android.database.Cursor;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.noel.videolist.R;
import com.example.noel.videolist.activity.base.BaseRecyclerListActivity;
import com.example.noel.videolist.activity.base.BaseRecyclerListAdapter;
import com.example.noel.videolist.data.VideoListContract;

import java.util.HashMap;

/**
 * Created by Noel on 4/23/2017.
 */

public class NestedListAdapter extends BaseRecyclerListAdapter {

    public NestedListAdapter(BaseRecyclerListActivity activity) {
        super(activity);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.item_nested_list, parent, false);
        return new NestedListAdapter.InnerListViewHolder(view);
    }

    private class InnerListViewHolder extends ActivityItemViewHolder {

        TextView contentTitle;
        RecyclerView innerRecyclerView;
        NestedListAdapterInner innerRecyclerViewAdapter;
        int topicId;

        InnerListViewHolder(View itemView) {
            super(itemView);
            contentTitle = (TextView) itemView.findViewById(R.id.item_nested_list_tv);

            innerRecyclerView = (RecyclerView) itemView.findViewById(R.id.item_nested_list_rv);
            LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
            innerRecyclerView.setLayoutManager(layoutManager);
            innerRecyclerViewAdapter = new NestedListAdapterInner(activity);
            innerRecyclerView.setAdapter(innerRecyclerViewAdapter);
        }

        @Override
        protected void onBind() {
            String title = cursor.getString(cursor.getColumnIndex(VideoListContract.TopicEntry.COLUMN_TITLE));
            contentTitle.setText(title);

            int topicId = cursor.getInt(cursor.getColumnIndex(VideoListContract.TopicEntry._ID));
            activity.getLoaderManager().initLoader(topicId, null, innerRecyclerViewAdapter);
        }

        @Override
        protected VideoListContract.Model createModel() {
            return new VideoListContract.TopicEntry(cursor);
        }

        @Override
        public void onClick(View v) {
            // Do nothing
        }
    }
}
