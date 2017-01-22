package com.ly.cpdemo;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
	public static String TAG = "TAG";
	private static int count = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.i(TAG, "Activity onCreate() thread:"
				+ Thread.currentThread().getName());

		Uri bookUri = Uri.parse("content://com.ly.cpdemo.book.provider/book");
		ContentValues values = new ContentValues();
		values.put("_id", 5);
		values.put("name", "Ruby");
		getContentResolver().insert(bookUri, values);

		Cursor bookCursor = getContentResolver().query(bookUri,
				new String[] { "_id", "name" }, null, null, null);
		while (bookCursor.moveToNext()) {
			Book book = new Book();
			book.bookId = bookCursor.getInt(0);
			book.bookName = bookCursor.getString(1);
			Log.i(TAG, "query book:" + book);
		}

		bookCursor.close();

		Uri userUri = Uri.parse("content://com.ly.cpdemo.book.provider/user");
		Cursor userCursor = getContentResolver().query(userUri,
				new String[] { "_id", "name", "sex" }, null, null, null);
		while (userCursor.moveToNext()) {
			count++;
			if (count == 10)
				break;
			User user = new User();
			user.userId = userCursor.getInt(0);
			user.userName = userCursor.getString(1);
			user.sex = userCursor.getInt(2);
			Log.i(TAG, "query user:" + user.toString());
		}

		userCursor.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
