package com.example.note.model.dataBase;
import com.example.note.model.dataBase.UserDataBaseHelper.Tables;
import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by gera on 28.09.2014.
 */
public class DataBaseContentProvider extends ContentProvider {
    // All URIs share these parts
    public static final String AUTHORITY = "com.example.note";
    public static final String SCHEME = "content://";
    // URIs
    public static final Uri URI_NOTE = Uri.parse(SCHEME + AUTHORITY + "/" + Tables.TABLE_DATA); // FIXME use buildUpon method here
    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        addURI(Tables.TABLE_DATA, QueryId.QUERY_TABLE_DATA);

    };
    private UserDataBaseHelper userDataBaseHelper;

    private static void addURI(String uri, QueryId query) {
        sURIMatcher.addURI(AUTHORITY, uri, query.ordinal());
    }

    private static QueryId matchQuery(Uri uri) {
        int id = sURIMatcher.match(uri);
        return id == -1 ? QueryId.NONE : QueryId.values()[id];
    }

    @Override
    public boolean onCreate() {
        userDataBaseHelper = new UserDataBaseHelper(getContext());
        return false;
    }

    private SelectionBuilder buildSimpleSelection(Uri uri) {
        final SelectionBuilder builder = new SelectionBuilder();
        // FIXME can be reworked with method in enum
        switch (matchQuery(uri)) {
            case QUERY_TABLE_DATA:
                return builder.table(Tables.TABLE_DATA);

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor result;
        SQLiteDatabase db = userDataBaseHelper.getReadableDatabase();
        switch (matchQuery(uri)) {
            default: {
                final SelectionBuilder builder = buildSimpleSelection(uri);
                result = builder.where(selection, selectionArgs).query(db, projection, sortOrder);
            }
        }
        result.setNotificationUri(getContext().getContentResolver(), uri);
        return result;
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = userDataBaseHelper.getWritableDatabase();
        final SelectionBuilder builder = buildSimpleSelection(uri);
        int rowsDeleted = builder.where(selection, selectionArgs).delete(db);

        if (rowsDeleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = userDataBaseHelper.getWritableDatabase();
        final SelectionBuilder builder = buildSimpleSelection(uri);
        int rowsUpdated = builder.where(selection, selectionArgs).update(db, values);

        if (rowsUpdated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    private String simpleGetTable(Uri uri) {
        // FIXME can be reworked with method in enum
        switch (matchQuery(uri)) {
            case QUERY_TABLE_DATA:
                return Tables.TABLE_DATA;


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
  /*  public Uri insert(Uri uri, ContentValues values) {
        Uri result;
        SQLiteDatabase db = userDataBaseHelper.getWritableDatabase();
        long id = 0;
        // FIXME can be reworked with method in enum
        switch (matchQuery(uri)) {
            case QUERY_TABLE_DATA:
                id = db.insertOrThrow(Tables.TABLE_DATA, null, values);
                // FIXME use buildUpon method here
                result = Uri.parse(URI_NOTE + "/" + id);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }*/


    public Uri insert(Uri uri, ContentValues values) {
        Uri result;
        String table = Tables.TABLE_DATA;
        final SQLiteDatabase db = userDataBaseHelper.getWritableDatabase();

        db.beginTransaction();
        try {
            try {
                long id = db.insertWithOnConflict(table, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                result = Uri.parse(URI_NOTE + "/" + id);
            } catch (SQLiteConstraintException e) {
                throw e;
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }
/* public Uri insert(Uri uri, ContentValues values) {
    Uri result;

	Log.d(TAG, uri.toString());
	Log.d(TAG, values.toString());

	SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

	long id = 0;

	switch (matchQuery(uri)) {
	case DB_NOTE:
	    id = db.insertOrThrow(DBNote.TABLE_NAME, null, values);
	    result = Uri.parse(URI_NOTE_TABLE + "/" + id);
	    break;

	default:
	    throw new UnsupportedOperationException("Unknown uri: " + uri);
	}
	getContext().getContentResolver().notifyChange(uri, null);

	return result;
    }*/

    public final int bulkInsert(Uri url, ContentValues[] values) {
        int result = 0;
        String table = simpleGetTable(url);
        final SQLiteDatabase db = userDataBaseHelper.getWritableDatabase();

        db.beginTransaction();
        try {
            for (ContentValues data : values) {
                try {
                    db.insertWithOnConflict(table, null, data, SQLiteDatabase.CONFLICT_REPLACE);
                    result++;
                } catch (SQLiteConstraintException e) {
                    throw e;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        getContext().getContentResolver().notifyChange(url, null);
        return result;
    }

    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        final SQLiteDatabase db = userDataBaseHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            final int numOperations = operations.size();
            final ContentProviderResult[] results = new ContentProviderResult[numOperations];
            for (int i = 0; i < numOperations; i++) {
                results[i] = operations.get(i).apply(this, results, i);
            }
            db.setTransactionSuccessful();
            return results;
        } finally {
            db.endTransaction();
        }
    }

    private enum QueryId {
        NONE,

        QUERY_TABLE_DATA

    }
}
