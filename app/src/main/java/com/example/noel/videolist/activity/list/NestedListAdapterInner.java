package com.example.noel.videolist.activity.list;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.noel.videolist.R;
import com.example.noel.videolist.activity.base.BaseRecyclerListActivity;
import com.example.noel.videolist.activity.base.BaseRecyclerListAdapter;
import com.example.noel.videolist.activity.content.ContentListAdapter;
import com.example.noel.videolist.data.VideoListContentProvider;
import com.example.noel.videolist.data.VideoListContract;
import com.example.noel.videolist.task.BitmapAsyncLoader;

import static com.example.noel.videolist.activity.list.NestedListActivity.BUNDLE_MODULE_ID;

/**
 * Created by Noel on 4/23/2017.
 */

public class NestedListAdapterInner extends BaseRecyclerListAdapter implements LoaderManager.LoaderCallbacks<Cursor> {

    public NestedListAdapterInner(BaseRecyclerListActivity activity) {
        super(activity);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.item_nested_list_inner, parent, false);
        return new NestedListAdapterInner.ContentItemViewHolder(view);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // id is moduleId so it is guaranteed to be unique
        if (id != 0) {
            return new CursorLoader(activity,
                    Uri.parse(VideoListContentProvider.TOPIC_URI + "/" + id),
                    null, null, null,
                    VideoListContract.ContentEntry.COLUMN_SEQ_NUM);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        swapCursor(null);
    }

    class ContentItemViewHolder extends ActivityItemViewHolder implements BitmapAsyncLoader.BitmapAsyncLoaderListener {

        TextView contentTitle;
        ImageView imageView;

        ContentItemViewHolder(View itemView) {
            super(itemView);
            contentTitle = (TextView) itemView.findViewById(R.id.item_nested_list_inner_tv);
            imageView = (ImageView) itemView.findViewById(R.id.item_nested_list_inner_im);
            itemView.setOnClickListener(this);
        }

        @Override
        protected void onBind() {
            String title = cursor.getString(cursor.getColumnIndex(VideoListContract.ContentEntry.COLUMN_TITLE));
            contentTitle.setText(title);

            String coverArtPath = activity.getString(R.string.path_cover_art) + cursor.getString(cursor.getColumnIndex(VideoListContract.ContentEntry.COLUMN_COVER_ART_PATH));
            BitmapAsyncLoader bitmapAsyncLoader = new BitmapAsyncLoader(this, activity.getAssets());
            bitmapAsyncLoader.setOutputSize(activity.getResources().getDimension(R.dimen.cover_art_width),
                    activity.getResources().getDimension(R.dimen.cover_art_height), activity.getResources().getDisplayMetrics().density);
            bitmapAsyncLoader.execute(coverArtPath);
        }

        @Override
        protected VideoListContract.Model createModel() {
            return new VideoListContract.ContentEntry(cursor);
        }

        @Override
        public void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }

        @Override
        public void onProgressUpdate(Integer... values) {
        }
    }
}