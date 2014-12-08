package com.opencbs.androidclient.repos;

import android.content.ContentValues;
import android.database.Cursor;

import com.opencbs.androidclient.models.EconomicActivity;

import javax.inject.Inject;

public class EconomicActivityRepo extends Repo<EconomicActivity> {

    @Inject
    public EconomicActivityRepo() {}

    @Override
    protected String getTableName() {
        return "economic_activities";
    }

    @Override
    protected ContentValues getContentValues(EconomicActivity item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", item.id);
        contentValues.put("name", item.name);
        contentValues.put("parent_id", item.parentId);
        return contentValues;
    }

    @Override
    protected EconomicActivity itemFromCursor(Cursor cursor) {
        EconomicActivity result = new EconomicActivity();
        result.id = cursor.getInt(cursor.getColumnIndex("_id"));
        result.name = cursor.getString(cursor.getColumnIndex("name"));
        result.parentId = cursor.getInt(cursor.getColumnIndex("parent_id"));
        return result;
    }
}
