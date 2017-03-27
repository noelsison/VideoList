package com.example.noel.videolist.activity.base;

import android.database.Cursor;
import android.provider.BaseColumns;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.noel.videolist.data.VideoListContract.Model;

/**
 * Created by Noel on 3/27/2017.
 */

public abstract class BaseRecyclerListAdapter<ActivityItemViewHolder> extends RecyclerView.Adapter<BaseRecyclerListAdapter.ActivityItemViewHolder> {

    protected BaseRecyclerListActivity activity;
    protected Cursor cursor;
    protected int layoutId;

    public BaseRecyclerListAdapter(BaseRecyclerListActivity activity, Cursor cursor, int layoutId) {
        this.activity = activity;
        this.cursor = cursor;
        this.layoutId = layoutId;
    }

    protected View inflateLayout(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        return inflater.inflate(layoutId, parent, false);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerListAdapter.ActivityItemViewHolder holder, int position) {
        if (cursor == null || !cursor.moveToPosition(position)) {
            return;
        }

        holder.onBind(position);
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

        if (newCursor != null) {
            cursor = newCursor;
            notifyDataSetChanged();
        } else {
            notifyItemRangeRemoved(0, getItemCount());
            cursor = null;
        }

        return oldCursor;
    }

    public interface ActivityListAdapterClickHandler {
        void onItemClick(Model model);
    }

    protected abstract class ActivityItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected int position;
        protected BaseColumns model;

        public ActivityItemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        private void onBind(int position) {
            this.position = position;
            onBind();
        }

        protected abstract void onBind();

        protected abstract Model createModel();

        @Override
        public void onClick(View v) {
            cursor.moveToPosition(position);
            Model model = createModel();
            activity.onItemClick(model);
        }
    }
}