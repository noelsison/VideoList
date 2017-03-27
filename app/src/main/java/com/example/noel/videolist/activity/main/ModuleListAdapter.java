package com.example.noel.videolist.activity.main;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.noel.videolist.R;
import com.example.noel.videolist.activity.base.BaseRecyclerListActivity;
import com.example.noel.videolist.activity.base.BaseRecyclerListAdapter;
import com.example.noel.videolist.data.VideoListContract;
import com.example.noel.videolist.data.VideoListContract.ModuleEntry;


/**
 * Created by Noel on 2/21/2017.
 */

public class ModuleListAdapter extends BaseRecyclerListAdapter {

    public ModuleListAdapter(BaseRecyclerListActivity activity) {
        super(activity);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.item_activity_main_list, parent, false);
        return new ModuleItemViewHolder(view);
    }

    class ModuleItemViewHolder extends ActivityItemViewHolder {

        TextView moduleTitle;
        String title;
        int moduleId;

        public ModuleItemViewHolder(View itemView) {
            super(itemView);
            moduleTitle = (TextView) itemView.findViewById(R.id.tv_main_list_item_title);
            itemView.setOnClickListener(this);
        }

        @Override
        protected void onBind() {
            moduleId = cursor.getInt(cursor.getColumnIndex(ModuleEntry._ID));
            title = cursor.getString(cursor.getColumnIndex(ModuleEntry.COLUMN_TITLE));
            moduleTitle.setText(title);
        }

        @Override
        protected VideoListContract.Model createModel() {
            return new ModuleEntry(cursor);
        }
    }
}
