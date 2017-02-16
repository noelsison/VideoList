package com.example.noel.videolist.data;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.example.noel.videolist.data.VideoListContract.MediaItemEntry;

/**
 * Created by Noel on 2/13/2017.
 */

public class TestUtil {

    public static void insertFakeData(SQLiteDatabase db) {
        if (db == null) return;

        List<ContentValues> list = new ArrayList<ContentValues>();

        list.add(makeContentValues("Big Buck Bunny", "big_buck_bunny"));
        list.add(makeContentValues("Cosmos Laundromat Trailer", "cosmos_laundromat_trailer"));
        list.add(makeContentValues("Sintel Trailer", "sintel_trailer"));

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

    private static ContentValues makeContentValues(String title, String filename) {
        ContentValues cv = new ContentValues();
        cv.put(MediaItemEntry.COLUMN_TITLE, title);
        cv.put(MediaItemEntry.COLUMN_FILENAME, filename);
        return cv;
    }
}
