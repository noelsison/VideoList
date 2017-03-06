package com.example.noel.videolist.content;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.noel.videolist.R;
import com.example.noel.videolist.data.DbConstants.ContentType;
import com.example.noel.videolist.data.VideoListContract.ContentItemEntry;


/**
 * Created by Noel on 3/6/2017.
 */

public class ContentListAdapter  extends RecyclerView.Adapter<ContentListAdapter.ContentItemViewHolder> {

    private final String TAG = ContentListAdapter.class.getName();

    private ContentListActivity activity;
    private Cursor cursor;

    public ContentListAdapter(ContentListActivity activity, Cursor cursor) {
        this.activity = activity;
        this.cursor = cursor;
    }

    @Override
    public ContentItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.content_list_item, parent, false);
        return new ContentItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContentItemViewHolder holder, int position) {
        if (cursor == null || !cursor.moveToPosition(position)) {
            return;
        }
        Integer intType = cursor.getInt(cursor.getColumnIndex(ContentItemEntry.COLUMN_TYPE));
        String type = ContentType.toString(intType);
        String title = cursor.getString(cursor.getColumnIndex(ContentItemEntry.COLUMN_TITLE));
        Integer contentId = cursor.getInt(cursor.getColumnIndex(ContentItemEntry.COLUMN_CONTENT_ID));

        Log.d(TAG, String.format("Position: %d, contentType: %s, title: %s, contentId: %d", position, type, title, contentId));

        // holder.activityType.setText(type);
        holder.activityTitle.setText(title);
        holder.contentType = intType;
        holder.contentId = contentId;
    }

    @Override
    public int getItemCount() {
        if (cursor != null) {
            return cursor.getCount();
        } else {
            return 0;
        }
    }

    public Cursor swapCursor(Cursor newCursor) {
        Cursor oldCursor = this.cursor;
        if (oldCursor == newCursor) {
            return null;
        }

        if(newCursor != null) {
            cursor = newCursor;
            notifyDataSetChanged();
        } else {
            notifyItemRangeRemoved(0, getItemCount());
            cursor = null;
        }

        return oldCursor;
    }

    public interface ActivityListAdapterClickHandler {
        void onItemClick(int type, int contentId);
    }

    class ContentItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // TextView activityType;
        TextView activityTitle;
        int contentType;
        int contentId;

        public ContentItemViewHolder(View itemView) {
            super(itemView);
            // activityType = (TextView) itemView.findViewById(R.id.tv_content_type);
            activityTitle = (TextView) itemView.findViewById(R.id.tv_content_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            activity.onItemClick(contentType, contentId);
        }
    }
}
