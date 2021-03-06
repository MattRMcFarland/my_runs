package com.example.mcfarland.myruns_sub1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	public static final String EXERCISE_ENTRY_TABLE = "entries";

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_INPUT_TYPE = "input_type";
	public static final String COLUMN_ACTIVITY_TYPE = "activity_type";
	public static final String COLUMN_DATE_TIME = "date_time";
	public static final String COLUMN_DURATION = "duration";
	public static final String COLUMN_DISTANCE = "distance";
	public static final String COLUMN_AVG_PACE = "avg_pace";
	public static final String COLUMN_AVG_SPEED = "avg_speed";
	public static final String COLUMN_CALORIES = "calories";
	public static final String COLUMN_CLIMB = "climb";
	public static final String COLUMN_HEART_RATE = "heart_rate";
	public static final String COLUMN_COMMENT = "comment";
	public static final String COLUMN_GPS_DATA = "gps_data";

	private static final String DATABASE_NAME = "ExerciseEntries.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ EXERCISE_ENTRY_TABLE + "(" +
			COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_INPUT_TYPE + " integer not null, "
			+ COLUMN_ACTIVITY_TYPE + " integer not null, "
			+ COLUMN_DATE_TIME + " datetime not null, "
			+ COLUMN_DURATION + " integer not null, "
			+ COLUMN_DISTANCE + " float, "
			+ COLUMN_AVG_PACE + " float, "
			+ COLUMN_AVG_SPEED + " float, "
			+ COLUMN_CALORIES + " integer, "
			+ COLUMN_CLIMB + " float, "
			+ COLUMN_HEART_RATE + " integer, "
			+ COLUMN_COMMENT + " text, "
			+ COLUMN_GPS_DATA + " blob " + " );";

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + EXERCISE_ENTRY_TABLE);
		onCreate(db);
	}

}