package com.opencbs.androidclient.repos;

import android.content.ContentValues;
import android.database.Cursor;

import com.opencbs.androidclient.models.City;

import javax.inject.Inject;

public class CityRepo extends Repo<City> {

    @Inject
    public CityRepo() {}

    @Override
    protected String getTableName() {
        return "cities";
    }

    @Override
    protected ContentValues getContentValues(City item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", item.id);
        contentValues.put("name", item.name);
        contentValues.put("district_id", item.districtId);
        return contentValues;
    }

    @Override
    protected City itemFromCursor(Cursor cursor) {
        City result = new City();
        result.id = cursor.getInt(cursor.getColumnIndex("_id"));
        result.name = cursor.getString(cursor.getColumnIndex("name"));
        result.districtId = cursor.getInt(cursor.getColumnIndex("district_id"));
        return result;
    }
}
