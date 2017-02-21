package com.example.noel.videolist;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.noel.videolist.data.DbConstants.ContentType;
import com.example.noel.videolist.data.VideoListContract.ContentItemEntry;


/**
 * Created by Noel on 2/21/2017.
 */

public class ActivityListAdapter extends RecyclerView.Adapter<ActivityListAdapter.ActivityItemViewHolder>{

    public final String TAG = ActivityListAdapter.class.getName();

    private MainActivity mActivity;
    private Cursor mCursor;

    public ActivityListAdapter(MainActivity activity, Cursor cursor) {
        this.mActivity = activity;
        this.mCursor = cursor;
        Log.d(TAG, "Cursor count: " + cursor.getCount());
    }

    @Override
    public ActivityItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View view = inflater.inflate(R.layout.activity_main_list_item, parent, false);
        return new ActivityItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ActivityItemViewHolder holder, int position) {
        if(!mCursor.moveToPosition(position)) return;

        Integer intType = mCursor.getInt(mCursor.getColumnIndex(ContentItemEntry.COLUMN_TYPE));
        String type = ContentType.toString(intType);
        String title = mCursor.getString(mCursor.getColumnIndex(ContentItemEntry.COLUMN_TITLE));
        Integer contentId = mCursor.getInt(mCursor.getColumnIndex(ContentItemEntry.COLUMN_CONTENT_ID));

        Log.d(TAG, String.format("Position: %d, contentType: %s, title: %s, contentId: %d", position, type, title, contentId));

        holder.activityType.setText(type);
        holder.activityTitle.setText(title);
        holder.contentType = intType;
        holder.contentId = contentId;
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public interface ActivityListAdapterClickHandler {
        void onClick(int type, int contentId);
    }

    class ActivityItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView activityType;
        TextView activityTitle;
        int contentType;
        int contentId;

        public ActivityItemViewHolder(View itemView) {
            super(itemView);
            activityType = (TextView) itemView.findViewById(R.id.tv_activity_type);
            activityTitle = (TextView) itemView.findViewById(R.id.tv_activity_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mActivity.onClick(contentType, contentId);
        }
    }
}
