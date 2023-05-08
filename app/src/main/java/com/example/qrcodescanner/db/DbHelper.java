package com.example.qrcodescanner.db;


import static com.example.qrcodescanner.db.EventContract.CatEntry.EVENT_START_DATE;
import static com.example.qrcodescanner.db.EventContract.CatEntry.EVENT_END_DATE;
import static com.example.qrcodescanner.db.EventContract.CatEntry.EVENT_ID;
import static com.example.qrcodescanner.db.EventContract.CatEntry.EVENT_NAME;
import static com.example.qrcodescanner.db.EventContract.CatEntry.TABLE_EVENT;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.qrcodescanner.retrofit.Event;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	// Database Name
	private static final String DATABASE_NAME = "qrscannerdb";
	// tasks table name

	private SQLiteDatabase dbase;
	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		dbase=db;

				db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_EVENT + " ("
				+ EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ EVENT_NAME + " TEXT,"
				+ EVENT_START_DATE + " TEXT,"
				+ EVENT_END_DATE + " TEXT"
				+ ");");


	}

	public void addEvents(List<Event> categories)
	{

		for (Event category: categories) {
			this.addEvent(category);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
		// Drop older table if existed

		db.execSQL("drop table " + TABLE_EVENT);


		onCreate(db);


	}




	// Adding new question

	public void addEvent(Event ev) {
		ContentValues values = new ContentValues();
		values.put(EVENT_NAME, ev.getEvent_name());
		values.put(EVENT_START_DATE, ev.getEvent_start());
		values.put(EVENT_END_DATE, ev.getEvent_end());
		// Inserting Row
		dbase.insert(TABLE_EVENT, null, values);
	}

	public ArrayList<Event> getAllEvents() {
		ArrayList<Event> catList = new ArrayList<>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_EVENT;
		dbase=this.getReadableDatabase();
		Cursor cursor = dbase.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Event cat = new Event();
				cat.setEvent_name(cursor.getString(1));
				cat.setEvent_start(cursor.getString(2));
				cat.setEvent_end(cursor.getString(3));
				catList.add(cat);
			} while (cursor.moveToNext());
		}

		return catList;
	}

	public void deleteAllEvents() {

		String selectQuery = "delete FROM " + TABLE_EVENT;
		dbase = this.getWritableDatabase();
		dbase.execSQL(selectQuery);
	}



}
