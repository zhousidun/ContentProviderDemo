package com.ly.cpdemo;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class BookProvider extends ContentProvider {
	public static String TAG = "TAG";

	public static final String AUTHORITY = "com.ly.cpdemo.book.provider";
	public static final Uri BOOK_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/book");
	public static final Uri USER_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/user");

	public static final int BOOK_URI_CODE = 0;
	public static final int USER_URI_CODE = 1;
	private static final UriMatcher sUriMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);
	
	private Context mContext;
	private SQLiteDatabase mDb;

	static {
		sUriMatcher.addURI(AUTHORITY, "book", BOOK_URI_CODE);
		sUriMatcher.addURI(AUTHORITY, "user", USER_URI_CODE);
	}

	private String getTableName(Uri uri) {
		String tableName = null;

		switch (sUriMatcher.match(uri)) {
		case BOOK_URI_CODE:
			tableName = DBOpenHelper.BOOK_TABLE_NAME;
			break;
		case USER_URI_CODE:
			tableName = DBOpenHelper.USER_TABLE_NAME;
			break;
		default:
			break;
		}

		return tableName;
	}

	@Override
	public boolean onCreate() {
		Log.i(TAG, "onCreate() thread:" + Thread.currentThread().getName());
		
		mContext  = getContext();
		
		initProviderData();
		
		return false;
	}

	private void initProviderData() {
		mDb = new DBOpenHelper(mContext).getWritableDatabase();
		mDb.execSQL("delete from "+ DBOpenHelper.BOOK_TABLE_NAME);
		mDb.execSQL("delete from "+DBOpenHelper.USER_TABLE_NAME);
		
		mDb.execSQL("insert into book values(1,'Java');");
		mDb.execSQL("insert into book values(2,'Php');");
		mDb.execSQL("insert into book values(3,'Python');");
		mDb.execSQL("insert into book values(4,'Node');");
		
		mDb.execSQL("insert into user values(1,'Bill Gates',1)");
		mDb.execSQL("insert into user values(2,'Bilibili',0)");
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Log.i(TAG, "query() thread:" + Thread.currentThread().getName());

		String table = getTableName(uri);
		if (table == null) {
			throw new IllegalArgumentException("Unsupported Uri: " + uri);
		}
		
		return mDb.query(table, projection,selection,selectionArgs,null,null,sortOrder,null);
	}

	@Override
	public String getType(Uri uri) {
		Log.i(TAG, "getType");
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Log.i(TAG, "insert");
		
		String table = getTableName(uri);
		if (table == null) {
			throw new IllegalArgumentException("Unsupported Uri: " + uri);
		}
		
		mDb.insert(table, null, values);
		mContext.getContentResolver().notifyChange(uri, null);
		return uri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		Log.i(TAG, "delete");
		
		String table = getTableName(uri);
		if (table == null) {
			throw new IllegalArgumentException("Unsupported Uri: " + uri);
		}
		
		int count = mDb.delete(table, selection, selectionArgs);
		if(count>0){
			getContext().getContentResolver().notifyChange(uri, null);
		}
		
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		Log.i(TAG, "update");
		
		String table = getTableName(uri);
		if (table == null) {
			throw new IllegalArgumentException("Unsupported Uri: " + uri);
		}
		
		int row = mDb.update(table, values, selection, selectionArgs);
		if(row>0){
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return row;
	}

}
