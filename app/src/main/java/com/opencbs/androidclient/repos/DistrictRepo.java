package com.opencbs.androidclient.repos;

import android.content.ContentValues;
import android.database.Cursor;

import com.opencbs.androidclient.models.District;

import javax.inject.Inject;

public class DistrictRepo extends Repo<District> {

    @Inject
    public DistrictRepo() {}

    @Override
    protected String getTableName() {
        return "districts";
    }

    @Override
    protected ContentValues getContentValues(District item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", item.id);
        contentValues.put("name", item.name);
        contentValues.put("region_id", item.regionId);
        return contentValues;
    }

    @Override
    protected District itemFromCursor(Cursor cursor) {
        District result = new District();
        result.id = cursor.getInt(cursor.getColumnIndex("_id"));
        result.name = cursor.getString(cursor.getColumnIndex("name"));
        result.regionId = cursor.getInt(cursor.getColumnIndex("region_id"));
        return result;
    }
}
