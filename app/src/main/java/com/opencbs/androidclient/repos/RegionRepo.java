package com.opencbs.androidclient.repos;

import android.content.ContentValues;
import android.database.Cursor;

import com.opencbs.androidclient.models.Region;

import javax.inject.Inject;

public class RegionRepo extends Repo<Region> {

    @Inject
    public RegionRepo() {}

    @Override
    protected String getTableName() {
        return "regions";
    }

    @Override
    protected ContentValues getContentValues(Region item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", item.id);
        contentValues.put("name", item.name);
        return contentValues;
    }

    @Override
    protected Region itemFromCursor(Cursor cursor) {
        Region result = new Region();
        result.id = cursor.getInt(cursor.getColumnIndex("_id"));
        result.name = cursor.getString(cursor.getColumnIndex("name"));
        return result;
    }
}
