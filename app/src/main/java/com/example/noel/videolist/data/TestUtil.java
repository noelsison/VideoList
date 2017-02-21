package com.example.noel.videolist.data;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


import com.example.noel.videolist.data.DbConstants.ContentType;
import com.example.noel.videolist.data.VideoListContract.ContentItemEntry;
import com.example.noel.videolist.data.VideoListContract.MediaItemEntry;

/**
 * Created by Noel on 2/13/2017.
 */

public class TestUtil {

    public static void insertFakeData(SQLiteDatabase db) {
        if (db == null) return;

        makeFakeContentItems(db);
        makeFakeMediaItems(db);
    }

    private static void makeFakeContentItems(SQLiteDatabase db) {
        List<ContentValues> list = new ArrayList<ContentValues>();

        // Fake ContentItem entries
        list.add(makeContentItemValues(ContentType.AUDIO_RECORD, "Audio Record Activity", 1));
        list.add(makeContentItemValues(ContentType.VIDEO, "Big Buck Bunny", 1));
        list.add(makeContentItemValues(ContentType.VIDEO, "Cosmos Laundromat Trailer", 2));
        list.add(makeContentItemValues(ContentType.VIDEO, "Sintel Trailer", 3));

        try {
            db.beginTransaction();
            db.delete(ContentItemEntry.TABLE_NAME, null, null);
            for (ContentValues values : list) {
                db.insert(ContentItemEntry.TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    private static void makeFakeMediaItems(SQLiteDatabase db) {
        List<ContentValues> list = new ArrayList<ContentValues>();
        // Fake MediaItem entries
        list.add(makeMediaItemValues(1, "Big Buck Bunny", "big_buck_bunny"));
        list.add(makeMediaItemValues(2, "Cosmos Laundromat Trailer", "cosmos_laundromat_trailer"));
        list.add(makeMediaItemValues(3, "Sintel Trailer", "sintel_trailer"));

        try {
            db.beginTransaction();
            db.delete(MediaItemEntry.TABLE_NAME, null, null);
            for (ContentValues values : list) {
                db.insert(MediaItemEntry.TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    private static ContentValues makeContentItemValues(Integer type, String title, Integer contentId) {
        ContentValues cv = new ContentValues();
        cv.put(ContentItemEntry.COLUMN_TYPE, type);
        cv.put(ContentItemEntry.COLUMN_TITLE, title);
        cv.put(ContentItemEntry.COLUMN_CONTENT_ID, contentId);
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
