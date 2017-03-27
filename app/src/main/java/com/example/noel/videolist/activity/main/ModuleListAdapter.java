package com.example.noel.videolist.activity.main;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.noel.videolist.R;
import com.example.noel.videolist.data.VideoListContract.ModuleEntry;


/**
 * Created by Noel on 2/21/2017.
 */

public class ModuleListAdapter extends RecyclerView.Adapter<ModuleListAdapter.ActivityItemViewHolder> {

    private final String TAG = ModuleListAdapter.class.getName();

    private MainActivity activity;
    private Cursor cursor;

    public ModuleListAdapter(MainActivity activity, Cursor cursor) {
        this.activity = activity;
        this.cursor = cursor;
    }

    @Override
    public ActivityItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.item_activity_main_list, parent, false);
        return new ActivityItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ActivityItemViewHolder holder, int position) {
        if (cursor == null || !cursor.moveToPosition(position)) {
            return;
        }
        Integer moduleId = cursor.getInt(cursor.getColumnIndex(ModuleEntry._ID));
        String title = cursor.getString(cursor.getColumnIndex(ModuleEntry.COLUMN_TITLE));

        Log.d(TAG, String.format("Position: %d, moduleId: %d, title: %s", position, moduleId, title));

        holder.update(moduleId, title);
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
        void onItemClick(int moduleId, String title);
    }

    class ActivityItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView moduleTitle;
        String title;
        int moduleId;

        public ActivityItemViewHolder(View itemView) {
            super(itemView);
            moduleTitle = (TextView) itemView.findViewById(R.id.tv_main_list_item_title);
            itemView.setOnClickListener(this);
        }

        public void update(int moduleId, String title) {
            this.moduleId = moduleId;
            this.title = title;
            moduleTitle.setText(title);
        }

        @Override
        public void onClick(View v) {
            activity.onItemClick(moduleId, title);
        }
    }
}
