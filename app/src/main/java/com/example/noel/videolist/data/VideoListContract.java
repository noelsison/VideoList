package com.example.noel.videolist.data;

import android.database.Cursor;
import android.provider.BaseColumns;

/**
 * Created by Noel on 2/13/2017.
 */

public class VideoListContract {

    public static class Model implements BaseColumns {
        protected int id;

        public Model(Cursor cursor) {
            id = cursor.getInt(cursor.getColumnIndex(_ID));
        }

        public int getId() {
            return id;
        }
    }

    public static final class ModuleEntry extends Model {
        public final static String TABLE_NAME = "module";
        public final static String COLUMN_TITLE = "title";

        private String title;

        public ModuleEntry(Cursor cursor) {
            super(cursor);
            title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
        }

        public String getTitle() {
            return title;
        }
    }

    public static final class ContentItemEntry extends Model {
        public final static String TABLE_NAME = "contentItem";
        public final static String COLUMN_MODULE_ID = "moduleId";
        public final static String COLUMN_TYPE = "type";
        public final static String COLUMN_TITLE = "title";
        public final static String COLUMN_CONTENT_ID = "contentId";
        public final static String COLUMN_SEQ_NUM= "seqNum";

        private int moduleId;
        private int type;
        private String title;
        private int contentId;
        private int seqNum;

        public ContentItemEntry(Cursor cursor) {
            super(cursor);
            moduleId = cursor.getInt(cursor.getColumnIndex(COLUMN_MODULE_ID));
            type = cursor.getInt(cursor.getColumnIndex(COLUMN_TYPE));
            title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
            contentId = cursor.getInt(cursor.getColumnIndex(COLUMN_CONTENT_ID));
            seqNum = cursor.getInt(cursor.getColumnIndex(COLUMN_SEQ_NUM));
        }

        public int getModuleId() {
            return moduleId;
        }

        public int getType() {
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

    public static final class MediaItemEntry extends Model {
        public final static String TABLE_NAME = "mediaItem";
        public final static String COLUMN_TITLE = "title";
        public final static String COLUMN_FILENAME = "filename";

        private String title;
        private String filename;

        public MediaItemEntry (Cursor cursor) {
            super(cursor);
            title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
            filename = cursor.getString(cursor.getColumnIndex(COLUMN_FILENAME));
        }

        public String getTitle() {
            return title;
        }

        public String getFilename() {
            return filename;
        }
    }
}
