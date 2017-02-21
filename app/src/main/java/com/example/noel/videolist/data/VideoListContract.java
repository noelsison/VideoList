package com.example.noel.videolist.data;

import android.provider.BaseColumns;

/**
 * Created by Noel on 2/13/2017.
 */

public class VideoListContract {

    public static final class ContentItemEntry implements BaseColumns {
        public final static String TABLE_NAME = "contentItem";
        public final static String COLUMN_TYPE = "type";
        public final static String COLUMN_TITLE = "title";
        public final static String COLUMN_CONTENT_ID = "contentId";

    }

    public static final class MediaItemEntry implements BaseColumns {
        public final static String TABLE_NAME = "mediaItem";
        public final static String COLUMN_TITLE = "title";
        public final static String COLUMN_FILENAME = "filename";
    }
}
