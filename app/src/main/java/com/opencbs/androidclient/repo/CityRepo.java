package com.opencbs.androidclient.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.opencbs.androidclient.DbHelper;
import com.opencbs.androidclient.model.City;

import java.util.ArrayList;

import javax.inject.Inject;

public class CityRepo {

    @Inject
    DbHelper dbHelper;

    @Inject
    public CityRepo() {}

    public void deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from cities");
    }

    public void add(City[] cities) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (City city : cities) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("_id", city.id);
            contentValues.put("name", city.name);
            contentValues.put("district_id", city.districtId);
            db.insert("cities", null, contentValues);
        }
    }
    public City get(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        City result = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from cities where _id = ?", new String[]{id + ""});
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                result = new City();
                result.id = id;
                result.name = cursor.getString(cursor.getColumnIndex("name"));
                result.districtId = cursor.getInt(cursor.getColumnIndex("district_id"));
            }
        } finally {
            cursor.close();
        }
        return result;
    }

    public City[] getAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<City> result = new ArrayList<City>();
        Cursor cursor = null;
        try
        {
            cursor = db.rawQuery("select * from cities", null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    City city = new City();
                    city.id = cursor.getInt(cursor.getColumnIndex("_id"));
                    city.name = cursor.getString(cursor.getColumnIndex("name"));
                    city.districtId = cursor.getInt(cursor.getColumnIndex("district_id"));
                    result.add(city);
                    cursor.moveToNext();
                }
            }
        } finally {
            cursor.close();
        }
        return result.toArray(new City[result.size()]);
    }
}
