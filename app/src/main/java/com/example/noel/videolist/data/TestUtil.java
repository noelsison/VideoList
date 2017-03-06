package com.example.noel.videolist.data;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


import com.example.noel.videolist.data.DbConstants.ContentType;
import com.example.noel.videolist.data.VideoListContract.ModuleEntry;
import com.example.noel.videolist.data.VideoListContract.ContentItemEntry;
import com.example.noel.videolist.data.VideoListContract.MediaItemEntry;

/**
 * Created by Noel on 2/13/2017.
 */

public class TestUtil {
    private static final String TAG = TestUtil.class.getName();

    public static void insertFakeData(SQLiteDatabase db) {
        if (db == null) return;
        Log.d(TAG, "Generating fake DB data");
        makeFakeModuleItems(db);
        makeFakeContentItems(db);
        makeFakeMediaItems(db);
        Log.d(TAG, "Done generating fake DB data");
    }

    private static void makeFakeModuleItems(SQLiteDatabase db) {
        List<ContentValues> list = new ArrayList<ContentValues>();
        // Fake MediaItem entries
        list.add(makeModuleValues(1, "Interview"));
        list.add(makeModuleValues(2, "Meeting"));
        list.add(makeModuleValues(3, "Business Correspondence"));
        list.add(makeModuleValues(4, "Presentation"));
        list.add(makeModuleValues(5, "Teamwork"));

        makeDbTransactions(db, ModuleEntry.TABLE_NAME, list);
    }

    private static void makeFakeContentItems(SQLiteDatabase db) {
        List<ContentValues> list = new ArrayList<ContentValues>();

        // Fake ContentItem entries
        list.add(makeContentItemValues(1, ContentType.AUDIO_RECORD, "Activity #1: Audio Record Activity", 1, 4));
        list.add(makeContentItemValues(1, ContentType.VIDEO, "Lesson #1: Big Buck Bunny", 1, 1));
        list.add(makeContentItemValues(1, ContentType.VIDEO, "Lesson #2: Cosmos Laundromat", 2, 2));
        list.add(makeContentItemValues(1, ContentType.VIDEO, "Lesson #3: Sintel", 3, 3));

        makeDbTransactions(db, ContentItemEntry.TABLE_NAME, list);
    }

    private static void makeFakeMediaItems(SQLiteDatabase db) {
        List<ContentValues> list = new ArrayList<ContentValues>();
        // Fake MediaItem entries
        list.add(makeMediaItemValues(1, "Big Buck Bunny", "big_buck_bunny"));
        list.add(makeMediaItemValues(2, "Cosmos Laundromat Trailer", "cosmos_laundromat_trailer"));
        list.add(makeMediaItemValues(3, "Sintel Trailer", "sintel_trailer"));

        makeDbTransactions(db, MediaItemEntry.TABLE_NAME, list);
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

    private static ContentValues makeContentItemValues(Integer moduleId, Integer type, String title, Integer contentId, Integer seqNum) {
        ContentValues cv = new ContentValues();
        cv.put(ContentItemEntry.COLUM_MODULE_ID, moduleId);
        cv.put(ContentItemEntry.COLUMN_TYPE, type);
        cv.put(ContentItemEntry.COLUMN_TITLE, title);
        cv.put(ContentItemEntry.COLUMN_CONTENT_ID, contentId);
        cv.put(ContentItemEntry.COLUMN_SEQ_NUM, seqNum);
        return cv;
    }

    private static ContentValues makeMediaItemValues(Integer id, String title, String filename) {
        ContentValues cv = new ContentValues();
        cv.put(MediaItemEntry._ID, id);
        cv.put(MediaItemEntry.COLUMN_TITLE, title);
        cv.put(MediaItemEntry.COLUMN_FILENAME, filename);
        return cv;
    }
}
