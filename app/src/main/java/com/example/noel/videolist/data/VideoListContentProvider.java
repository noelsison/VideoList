package com.example.noel.videolist.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.noel.videolist.data.VideoListContract.ContentItemEntry;
import com.example.noel.videolist.data.VideoListContract.MediaItemEntry;
import com.example.noel.videolist.data.VideoListContract.ModuleEntry;

/**
 * Created by Noel on 2/27/2017.
 */

public class VideoListContentProvider extends ContentProvider {

    private static final String TAG = VideoListContentProvider.class.getName();

    private static final int MODULE_ALL = 1;
    private static final int MODULE_CONTENT = 2;
    private static final int CONTENT_ALL = 3;
    private static final int CONTENT_ITEM = 4;
    private static final int MEDIA_ALL = 5;
    private static final int MEDIA_ITEM = 6;

    private static final String AUTHORITY = "com.example.noel.videolist";

    private static final String MODULE_PATH = "module";
    private static final String CONTENT_PATH = "content";
    private static final String MEDIA_PATH = "media";

    // Public URIs
    public static final Uri MODULE_URI = Uri.parse(String.format("content://%s/%s", AUTHORITY, MODULE_PATH));
    public static final Uri CONTENT_URI = Uri.parse(String.format("content://%s/%s", AUTHORITY, CONTENT_PATH));
    public static final Uri MEDIA_URI = Uri.parse(String.format("content://%s/%s", AUTHORITY, MEDIA_PATH));

    // String format
    public static final String TYPE_FORMAT = "%s/vnd.example.noel.videolist.%s";

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, MODULE_PATH, MODULE_ALL);
        uriMatcher.addURI(AUTHORITY, MODULE_PATH + "/#", MODULE_CONTENT);
        uriMatcher.addURI(AUTHORITY, CONTENT_PATH, CONTENT_ALL);
        uriMatcher.addURI(AUTHORITY, CONTENT_PATH + "/#", CONTENT_ITEM);
        uriMatcher.addURI(AUTHORITY, MEDIA_PATH, MEDIA_ALL);
        uriMatcher.addURI(AUTHORITY, MEDIA_PATH + "/*", MEDIA_ITEM);
    }

    private static final String unsupportedUri = "Unsupported URI: ";


    SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        db = VideoListDbHelper.getInstance(getContext()).getWritableDatabase();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "Query: " + uri);
        String tableName = null;
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch (uriMatcher.match(uri)) {
            default:
                throw new IllegalArgumentException(unsupportedUri + uri);
            case MODULE_ALL:
                queryBuilder.setTables(ModuleEntry.TABLE_NAME);
                break;
            case MODULE_CONTENT:
                queryBuilder.setTables(ContentItemEntry.TABLE_NAME);
                queryBuilder.appendWhere(ContentItemEntry.COLUM_MODULE_ID + " = " + uri.getLastPathSegment());
                // Use appendWhereEscapeString for user input
                break;
            case MEDIA_ITEM:
                queryBuilder.setTables(MediaItemEntry.TABLE_NAME);
                queryBuilder.appendWhere("_ID = " + uri.getLastPathSegment());
                break;
        }

        Cursor cursor = null;

        try {
            cursor = db.query(tableName,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case MODULE_ALL:
                return String.format(TYPE_FORMAT, ContentResolver.CURSOR_DIR_BASE_TYPE, MODULE_PATH);
            case MODULE_CONTENT:
                return String.format(TYPE_FORMAT, ContentResolver.CURSOR_ITEM_BASE_TYPE, MODULE_PATH);
            case CONTENT_ALL:
                return String.format(TYPE_FORMAT, ContentResolver.CURSOR_DIR_BASE_TYPE, CONTENT_PATH);
            case MEDIA_ITEM:
                return String.format(TYPE_FORMAT, ContentResolver.CURSOR_ITEM_BASE_TYPE, MEDIA_PATH);
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
