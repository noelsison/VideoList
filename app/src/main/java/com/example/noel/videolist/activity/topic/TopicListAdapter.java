package com.example.noel.videolist.activity.topic;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.noel.videolist.R;
import com.example.noel.videolist.activity.base.BaseRecyclerListActivity;
import com.example.noel.videolist.activity.base.BaseRecyclerListAdapter;
import com.example.noel.videolist.data.VideoListContract.Model;
import com.example.noel.videolist.data.VideoListContract.TopicEntry;

/**
 * Created by Noel on 4/10/2017.
 */

public class TopicListAdapter extends BaseRecyclerListAdapter {

    public TopicListAdapter(BaseRecyclerListActivity activity) {
        super(activity);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.item_content_list, parent, false);
        return new TopicItemViewHolder(view);
    }

    class TopicItemViewHolder extends ActivityItemViewHolder {

        TextView contentTitle;

        public TopicItemViewHolder(View itemView) {
            super(itemView);
            contentTitle = (TextView) itemView.findViewById(R.id.tv_content_list_item_title);
            itemView.setOnClickListener(this);
        }

        @Override
        protected void onBind() {
            String title = cursor.getString(cursor.getColumnIndex(TopicEntry.COLUMN_TITLE));
            contentTitle.setText(title);
        }

        @Override
        protected Model createModel() {
            return new TopicEntry(cursor);
        }
    }
}