package com.opencbs.androidclient.repos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.opencbs.androidclient.models.CustomField;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CustomFieldRepo extends Repo<CustomField> {

    @Inject
    public CustomFieldRepo() {}

    public List<CustomField> getPersonCustomFields() {
        return getCustomFieldsByOwner("Person");
    }

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

    private List<CustomField> getCustomFieldsByOwner(String owner) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<CustomField> result = new ArrayList<CustomField>();
        Cursor cursor = null;
        try
        {
            String query = "select * from custom_fields " +
                    "where owner = ? " +
                    "order by tab, field_order";
            cursor = db.rawQuery(query, new String[] { owner });
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    CustomField item = itemFromCursor(cursor);
                    result.add(item);
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
