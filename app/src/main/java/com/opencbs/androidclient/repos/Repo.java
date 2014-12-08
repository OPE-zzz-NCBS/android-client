package com.opencbs.androidclient.repos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public abstract class Repo<T> {

    @Inject
    DbHelper dbHelper;

    public void deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from " + getTableName());
    }

    public void add(List<T> items) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (T item : items) {
            ContentValues contentValues = getContentValues(item);
            db.insert(getTableName(), null, contentValues);
        }
    }

    public T get(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        T result = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from " + getTableName() + " where _id = ?", new String[]{id + ""});
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                result = itemFromCursor(cursor);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    public List<T> getAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<T> result = new ArrayList<T>();
        Cursor cursor = null;
        try
        {
            cursor = db.rawQuery("select * from " + getTableName(), null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    T item = itemFromCursor(cursor);
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

    protected abstract String getTableName();
    protected abstract ContentValues getContentValues(T item);
    protected abstract T itemFromCursor(Cursor cursor);
}
