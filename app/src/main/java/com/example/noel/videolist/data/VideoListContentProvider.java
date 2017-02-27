package com.example.noel.videolist.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.noel.videolist.data.VideoListContract.ContentItemEntry;
import com.example.noel.videolist.data.VideoListContract.MediaItemEntry;

/**
 * Created by Noel on 2/27/2017.
 */

public class VideoListContentProvider extends ContentProvider {

    private static final String TAG = VideoListContentProvider.class.getName();

    private static final int CONTENT_ALL = 1;
    private static final int CONTENT_ITEM = 2;
    private static final int MEDIA_ALL = 3;
    private static final int MEDIA_ITEM = 4;

    private static final String AUTHORITY = "com.example.noel.videolist";

    private static final String CONTENT_PATH = "content";
    private static final String MEDIA_PATH = "media";

    // Public URIs
    public static final Uri CONTENT_URI = Uri.parse(String.format("content://%s/%s", AUTHORITY, CONTENT_PATH));
    public static final Uri MEDIA_URI = Uri.parse(String.format("content://%s/%s", AUTHORITY, MEDIA_PATH));

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, CONTENT_PATH, CONTENT_ALL);
        uriMatcher.addURI(AUTHORITY, CONTENT_PATH + "/#", CONTENT_ITEM);
        uriMatcher.addURI(AUTHORITY, MEDIA_PATH, MEDIA_ALL);
        uriMatcher.addURI(AUTHORITY, MEDIA_PATH + "/*", MEDIA_ITEM);
    }

    private static final String unsupportedUri = "Unsupported URI: ";
    private static final String mimeItem = "cnd.android.cursor.item";
    private static final String mimeDir = "cnd.android.cursor.dir";


    SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        db = VideoListDbHelper.getInstance(getContext()).getWritableDatabase();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String tableName = null;
        switch (uriMatcher.match(uri)) {
            default:
                throw new IllegalArgumentException(unsupportedUri + uri);
            case CONTENT_ALL:
                tableName = ContentItemEntry.TABLE_NAME;
                break;
            case MEDIA_ITEM:
                tableName = MediaItemEntry.TABLE_NAME;
                if (selection == null) {
                    selection = "_ID = " + uri.getLastPathSegment();
                }
                break;
        }

        return db.query(tableName,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case CONTENT_ALL:
                return String.format("%s/vnd.%s", mimeDir, CONTENT_URI);
            case MEDIA_ITEM:
                return String.format("%s/vnd.%s", mimeItem, MEDIA_URI);
            default:
                throw new IllegalArgumentException(unsupportedUri + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
