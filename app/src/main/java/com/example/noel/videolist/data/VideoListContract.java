package com.example.noel.videolist.data;

import android.provider.BaseColumns;

/**
 * Created by Noel on 2/13/2017.
 */

public class VideoListContract {

    public static final class MediaItemEntry implements BaseColumns {
        public final static String TABLE_NAME ="mediaItem";
        public final static String COLUMN_TITLE = "title";
        public final static String COLUMN_FILENAME = "filename";
    }
}
