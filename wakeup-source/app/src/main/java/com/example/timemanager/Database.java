package com.example.timemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class Database {
    private static final String DATABASE_NAME = "alarms.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "alarmTable";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_HOUR = "hour";
    private static final String COLUMN_MINUTE = "minute";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_ACTIVE = "active";
    private static final String COLUMN_CYCLE = "cycle";

    private static final int NUM_COLUMN_ID = 0;
    private static final int NUM_COLUMN_HOUR = 1;
    private static final int NUM_COLUMN_MINUTE = 2;
    private static final int NUM_COLUMN_DESCRIPTION = 3;
    private static final int NUM_COLUMN_ACTIVE = 4;
    private static final int NUM_COLUMN_CYCLE = 5;

    private SQLiteDatabase mDataBase;

    public Database(Context context) {
        OpenHelper mOpenHelper = new OpenHelper(context);
        mDataBase = mOpenHelper.getWritableDatabase();
    }

    public long insert(int hour, int minute, String description, boolean isActive, boolean isCycle) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_HOUR, hour);
        cv.put(COLUMN_MINUTE, minute);
        cv.put(COLUMN_DESCRIPTION, description);
        cv.put(COLUMN_ACTIVE, isActive);
        cv.put(COLUMN_CYCLE, isCycle);
        return mDataBase.insert(TABLE_NAME, null, cv);
    }

    public int update(Event ev) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_HOUR, ev.getHour());
        cv.put(COLUMN_MINUTE, ev.getMinute());
        cv.put(COLUMN_DESCRIPTION, ev.getDescription());
        cv.put(COLUMN_ACTIVE, ev.getIsActive());
        cv.put(COLUMN_CYCLE, ev.isCycle());
        return mDataBase.update(TABLE_NAME, cv, COLUMN_ID + " = ?", new String[]{String.valueOf(ev.getId())});
    }

    public void deleteAll() {
        mDataBase.delete(TABLE_NAME, null, null);
    }

    public void delete(long id) {
        mDataBase.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public Event select(int id) {
        Cursor mCursor = mDataBase.query(TABLE_NAME, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        mCursor.moveToFirst();

        int hour = mCursor.getInt(NUM_COLUMN_HOUR);
        int minute = mCursor.getInt(NUM_COLUMN_MINUTE);
        String description = mCursor.getString(NUM_COLUMN_DESCRIPTION);
        boolean isActive = mCursor.getInt(NUM_COLUMN_ACTIVE) != 0;
        boolean isCycle = mCursor.getInt(NUM_COLUMN_CYCLE) != 0;

        return new Event(id, hour, minute, description, isActive, isCycle);
    }

    public ArrayList<Event> selectAll() {
        Cursor mCursor = mDataBase.query(TABLE_NAME, null, null, null, null, null, null);

        ArrayList<Event> arr = new ArrayList<Event>();
        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                int id = mCursor.getInt(NUM_COLUMN_ID);
                int hour = mCursor.getInt(NUM_COLUMN_HOUR);
                int minute = mCursor.getInt(NUM_COLUMN_MINUTE);
                String description = mCursor.getString(NUM_COLUMN_DESCRIPTION);
                boolean isActive = mCursor.getInt(NUM_COLUMN_ACTIVE) != 0;
                boolean isCycle = mCursor.getInt(NUM_COLUMN_CYCLE) != 0;
                arr.add(new Event(id, hour, minute, description, isActive, isCycle));
            } while (mCursor.moveToNext());
        }
        return arr;
    }

    private class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String query = "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_HOUR + " INT, " +
                    COLUMN_MINUTE + " INT, " +
                    COLUMN_DESCRIPTION + " TEXT," +
                    COLUMN_ACTIVE + " BOOLEAN," +
                    COLUMN_CYCLE + " BOOLEAN);";
            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}