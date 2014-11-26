package com.opencbs.androidclient.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.opencbs.androidclient.DbHelper;
import com.opencbs.androidclient.model.Region;

import java.util.ArrayList;

import javax.inject.Inject;

public class RegionRepo {

    @Inject
    DbHelper dbHelper;

    @Inject
    public RegionRepo() {}

    public void deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from regions");
    }

    public void add(Region[] regions) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (Region region : regions) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("_id", region.id);
            contentValues.put("name", region.name);
            db.insert("regions", null, contentValues);
        }
    }

    public Region get(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Region result = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from regions where _id = ?", new String[]{id + ""});
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                result = new Region();
                result.id = id;
                result.name = cursor.getString(cursor.getColumnIndex("name"));
            }
        } finally {
            cursor.close();
        }
        return result;
    }

    public Region[] getAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<Region> result = new ArrayList<Region>();
        Cursor cursor = null;
        try
        {
            cursor = db.rawQuery("select * from regions", null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Region region = new Region();
                    region.id = cursor.getInt(cursor.getColumnIndex("_id"));
                    region.name = cursor.getString(cursor.getColumnIndex("name"));
                    result.add(region);
                    cursor.moveToNext();
                }
            }
        } finally {
            cursor.close();
        }
        return result.toArray(new Region[result.size()]);
    }
}
