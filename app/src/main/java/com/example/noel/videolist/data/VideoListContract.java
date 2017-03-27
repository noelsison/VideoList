package com.example.noel.videolist.data;

import android.database.Cursor;
import android.provider.BaseColumns;

/**
 * Created by Noel on 2/13/2017.
 */

public class VideoListContract {

    public static final class ModuleEntry implements BaseColumns {
        public final static String TABLE_NAME = "module";
        public final static String COLUMN_TITLE = "title";

        private int id;
        private String title;

        public ModuleEntry (Cursor cursor) {
            id = cursor.getInt(cursor.getColumnIndex(_ID));
            title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }
    }

    public static final class ContentItemEntry implements BaseColumns {
        public final static String TABLE_NAME = "contentItem";
        public final static String COLUMN_MODULE_ID = "moduleId";
        public final static String COLUMN_TYPE = "type";
        public final static String COLUMN_TITLE = "title";
        public final static String COLUMN_CONTENT_ID = "contentId";
        public final static String COLUMN_SEQ_NUM= "seqNum";

        private int id;
        private int moduleId;
        private String type;
        private String title;
        private int contentId;
        private int seqNum;

        public ContentItemEntry(Cursor cursor) {
            id = cursor.getInt(cursor.getColumnIndex(_ID));
            moduleId = cursor.getInt(cursor.getColumnIndex(COLUMN_MODULE_ID));
            type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));
            title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
            contentId = cursor.getInt(cursor.getColumnIndex(COLUMN_CONTENT_ID));
            seqNum = cursor.getInt(cursor.getColumnIndex(COLUMN_SEQ_NUM));
        }

        public int getId() {
            return id;
        }

        public int getModuleId() {
            return moduleId;
        }

        public String getType() {
            return type;
        }

        public String getTitle() {
            return title;
        }

        public int getContentId() {
            return contentId;
        }

        public int getSeqNum() {
            return seqNum;
        }
    }

    public static final class MediaItemEntry implements BaseColumns {
        public final static String TABLE_NAME = "mediaItem";
        public final static String COLUMN_TITLE = "title";
        public final static String COLUMN_FILENAME = "filename";

        private int id;
        private String title;
        private String filename;

        public MediaItemEntry (Cursor cursor) {
            id = cursor.getInt(cursor.getColumnIndex(_ID));
            title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
            filename = cursor.getString(cursor.getColumnIndex(COLUMN_FILENAME));
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getFilename() {
            return filename;
        }
    }
}
