package com.opencbs.androidclient.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.opencbs.androidclient.DbHelper;
import com.opencbs.androidclient.model.District;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DistrictRepo {

    @Inject
    DbHelper dbHelper;

    @Inject
    public DistrictRepo() {}

    public void deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from districts");
    }

    public void add(List<District> districts) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (District district : districts) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("_id", district.id);
            contentValues.put("name", district.name);
            contentValues.put("region_id", district.regionId);
            db.insert("districts", null, contentValues);
        }
    }

    public District get(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        District result = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from districts where _id = ?", new String[]{id + ""});
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                result = new District();
                result.id = id;
                result.name = cursor.getString(cursor.getColumnIndex("name"));
                result.regionId = cursor.getInt(cursor.getColumnIndex("region_id"));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    public List<District> getAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<District> result = new ArrayList<District>();
        Cursor cursor = null;
        try
        {
            cursor = db.rawQuery("select * from districts", null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    District district = new District();
                    district.id = cursor.getInt(cursor.getColumnIndex("_id"));
                    district.name = cursor.getString(cursor.getColumnIndex("name"));
                    district.regionId = cursor.getInt(cursor.getColumnIndex("region_id"));
                    result.add(district);
                    cursor.moveToNext();
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }
}
