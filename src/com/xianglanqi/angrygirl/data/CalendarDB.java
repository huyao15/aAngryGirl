package com.xianglanqi.angrygirl.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xianglanqi.angrygirl.model.CalendarCell;
import com.xianglanqi.angrygirl.model.CellType;
import com.xianglanqi.angrygirl.model.Mood;

public class CalendarDB extends SQLiteOpenHelper {

    public static final String DB_NAME = "mood.db";

    public static final int version = 1;

    public CalendarDB(Context context) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE mood (" + "year int(11), " + "month int(11), " + "day int(11), "
                + "mood_code varchar(64), " + "updated_time timestamp);";
        db.execSQL(sql);

        sql = "CREATE UNIQUE INDEX y_m_d ON mood (`year`, `month`, `day`)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        db.execSQL("drop table mood");
        onCreate(db);
    }

    public void changeMood(CalendarCell cell) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("year", cell.getYear());
        cv.put("month", cell.getMonth());
        cv.put("day", cell.getDay());
        cv.put("updated_time", cell.getUpdatedTime().getTime());
        cv.put("mood_code", cell.getMood().getCode());

        db.insertWithOnConflict("mood", null, cv, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public Map<String, CalendarCell> getMood(int year, int month) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query("mood", new String[] { "mood_code", "day", "updated_time" }, "year=? AND month=?",
                new String[] { "" + year, "" + month }, null, null, null);
        Map<String, CalendarCell> map = new HashMap<String, CalendarCell>();
        try {
            while (cursor.moveToNext()) {
                int day = cursor.getInt(cursor.getColumnIndex("day"));
                String moodCode = cursor.getString(cursor.getColumnIndex("mood_code"));
                long updatedTime = cursor.getLong(cursor.getColumnIndex("updated_time"));

                String key = year + "_" + month + "_" + day;
                CalendarCell cell = new CalendarCell(year, month, day, "", CellType.CELL_DAY);
                cell.setMood(Mood.getMood(moodCode));
                cell.setUpdatedTime(new Date(updatedTime));
                map.put(key, cell);
            }

        } finally {
            cursor.close();
        }
        return map;
    }
}
