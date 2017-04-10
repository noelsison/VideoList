package com.example.noel.videolist.data;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


import com.example.noel.videolist.data.DbConstants.ContentType;
import com.example.noel.videolist.data.VideoListContract.TopicEntry;
import com.example.noel.videolist.data.VideoListContract.ModuleEntry;
import com.example.noel.videolist.data.VideoListContract.ContentEntry;
import com.example.noel.videolist.data.VideoListContract.MediaEntry;

/**
 * Created by Noel on 2/13/2017.
 */

public class TestUtil {
    private static final String TAG = TestUtil.class.getName();

    public static void insertFakeData(SQLiteDatabase db) {
        if (db == null) return;
        Log.d(TAG, "Generating fake DB data");
        makeFakeModuleItems(db);
        makeFakeTopicItems(db);
        makeFakeContentItems(db);
        makeFakeMediaItems(db);
        Log.d(TAG, "Done generating fake DB data");
    }

    private static void makeFakeModuleItems(SQLiteDatabase db) {
        List<ContentValues> list = new ArrayList<ContentValues>();
        // Fake MediaItem entries
        list.add(makeModuleValues(1, "Interview"));
        list.add(makeModuleValues(2, "Presentation"));
        list.add(makeModuleValues(3, "Meeting"));
        list.add(makeModuleValues(4, "Teamwork"));
        list.add(makeModuleValues(5, "Business Correspondence"));

        makeDbTransactions(db, ModuleEntry.TABLE_NAME, list);
    }

    private static void makeFakeTopicItems(SQLiteDatabase db) {
        List<ContentValues> list = new ArrayList<ContentValues>();
        // Fake MediaItem entries
        list.add(makeTopicitems(1, 1, "Look Presentable", 1));
        list.add(makeTopicitems(2, 1, "Questions Ahead", 2));
        list.add(makeTopicitems(3, 2, "Making an Impact", 1));

        makeDbTransactions(db, TopicEntry.TABLE_NAME, list);
    }

    private static void makeFakeContentItems(SQLiteDatabase db) {
        List<ContentValues> list = new ArrayList<ContentValues>();

        // Fake ContentItem entries
        list.add(makeContentItemValues(1, ContentType.VIDEO, "Lesson: Proper Attire", 1, 1));
        list.add(makeContentItemValues(2, ContentType.VIDEO, "Lesson: Common Interview Questions", 2, 1));
        list.add(makeContentItemValues(2, ContentType.AUDIO_RECORD, "Activity: Your First Interview", 1, 2));
        list.add(makeContentItemValues(3, ContentType.VIDEO, "Lesson: Formatting Slides", 3, 1));

        makeDbTransactions(db, ContentEntry.TABLE_NAME, list);
    }

    private static void makeFakeMediaItems(SQLiteDatabase db) {
        List<ContentValues> list = new ArrayList<ContentValues>();
        // Fake MediaItem entries
        list.add(makeMediaItemValues(1, "Big Buck Bunny", "big_buck_bunny"));
        list.add(makeMediaItemValues(2, "Cosmos Laundromat Trailer", "cosmos_laundromat_trailer"));
        list.add(makeMediaItemValues(3, "Sintel Trailer", "sintel_trailer"));

        makeDbTransactions(db, MediaEntry.TABLE_NAME, list);
    }

    private static void makeDbTransactions(SQLiteDatabase db, String tableName, List<ContentValues> list) {
        try {
            db.beginTransaction();
            db.delete(tableName, null, null);
            for (ContentValues values : list) {
                db.insert(tableName, null, values);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    private static ContentValues makeModuleValues(Integer id, String title) {
        ContentValues cv = new ContentValues();
        cv.put(ModuleEntry._ID, id);
        cv.put(ModuleEntry.COLUMN_TITLE, title);
        return cv;
    }

    private static ContentValues makeTopicitems(Integer id, Integer moduleId, String title, Integer seqNum) {
        ContentValues cv = new ContentValues();
        cv.put(TopicEntry._ID, id);
        cv.put(TopicEntry.COLUMN_MODULE_ID, moduleId);
        cv.put(TopicEntry.COLUMN_TITLE, title);
        cv.put(TopicEntry.COLUMN_SEQ_NUM, seqNum);
        return cv;
    }

    private static ContentValues makeContentItemValues(Integer topicId, Integer type, String title, Integer contentId, Integer seqNum) {
        ContentValues cv = new ContentValues();
        cv.put(ContentEntry.COLUMN_TOPIC_ID, topicId);
        cv.put(ContentEntry.COLUMN_TYPE, type);
        cv.put(ContentEntry.COLUMN_TITLE, title);
        cv.put(ContentEntry.COLUMN_CONTENT_ID, contentId);
        cv.put(ContentEntry.COLUMN_SEQ_NUM, seqNum);
        return cv;
    }

    private static ContentValues makeMediaItemValues(Integer id, String title, String filename) {
        ContentValues cv = new ContentValues();
        cv.put(MediaEntry._ID, id);
        cv.put(MediaEntry.COLUMN_TITLE, title);
        cv.put(MediaEntry.COLUMN_FILENAME, filename);
        return cv;
    }
}
