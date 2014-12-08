package com.opencbs.androidclient.repos;

import android.content.ContentValues;
import android.database.Cursor;

import com.opencbs.androidclient.models.Branch;

import javax.inject.Inject;

public class BranchRepo extends Repo<Branch> {

    @Inject
    public BranchRepo() {
    }

    @Override
    protected String getTableName() {
        return "branches";
    }

    @Override
    protected ContentValues getContentValues(Branch item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", item.id);
        contentValues.put("name", item.name);
        contentValues.put("code", item.code);
        contentValues.put("description", item.description);
        contentValues.put("address", item.address);
        return contentValues;
    }

    @Override
    protected Branch itemFromCursor(Cursor cursor) {
        Branch result = new Branch();
        result.id = cursor.getInt(cursor.getColumnIndex("_id"));
        result.name = cursor.getString(cursor.getColumnIndex("name"));
        result.code = cursor.getString(cursor.getColumnIndex("code"));
        result.description = cursor.getString(cursor.getColumnIndex("description"));
        result.address = cursor.getString(cursor.getColumnIndex("address"));
        return result;
    }
}
