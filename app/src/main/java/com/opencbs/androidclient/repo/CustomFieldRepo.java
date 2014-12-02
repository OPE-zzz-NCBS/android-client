package com.opencbs.androidclient.repo;

import android.content.ContentValues;
import android.database.Cursor;

import com.opencbs.androidclient.model.CustomField;

import javax.inject.Inject;

public class CustomFieldRepo extends Repo<CustomField> {

    @Inject
    public CustomFieldRepo() {}

    @Override
    protected String getTableName() {
        return "custom_fields";
    }

    @Override
    protected ContentValues getContentValues(CustomField item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", item.id);
        contentValues.put("caption", item.caption);
        contentValues.put("type", item.type);
        contentValues.put("owner", item.owner);
        contentValues.put("tab", item.tab);
        contentValues.put("is_unique", item.unique ? 1 : 0);
        contentValues.put("required", item.mandatory ? 1 : 0);
        contentValues.put("field_order", item.order);
        contentValues.put("extra", item.extra);
        return contentValues;
    }

    @Override
    protected CustomField itemFromCursor(Cursor cursor) {
        CustomField field = new CustomField();
        field.id = cursor.getInt(cursor.getColumnIndex("_id"));
        field.caption = cursor.getString(cursor.getColumnIndex("caption"));
        field.type = cursor.getString(cursor.getColumnIndex("type"));
        field.owner = cursor.getString(cursor.getColumnIndex("owner"));
        field.tab = cursor.getString(cursor.getColumnIndex("tab"));
        field.unique = cursor.getInt(cursor.getColumnIndex("is_unique")) == 1;
        field.mandatory = cursor.getInt(cursor.getColumnIndex("required")) == 1;
        field.order = cursor.getInt(cursor.getColumnIndex("field_order"));
        field.extra = cursor.getString(cursor.getColumnIndex("extra"));
        return field;
    }
}
