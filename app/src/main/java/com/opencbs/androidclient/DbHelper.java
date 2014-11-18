package com.opencbs.androidclient;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.opencbs.androidclient.model.EconomicActivity;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cache.db";
    private static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table economic_activities (" +
                        "_id integer primary key, " +
                        "name text not null, " +
                        "parent_id integer not null" +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void deleteEconomicActivities() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from economic_activities");
    }

    public void addEconomicActivities(EconomicActivity[] economicActivities) {
        SQLiteDatabase db = getWritableDatabase();
        for (EconomicActivity ea : economicActivities) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("_id", ea.id);
            contentValues.put("name", ea.name);
            contentValues.put("parent_id", ea.parentId);
            db.insert("economic_activities", null, contentValues);
        }
    }
}
