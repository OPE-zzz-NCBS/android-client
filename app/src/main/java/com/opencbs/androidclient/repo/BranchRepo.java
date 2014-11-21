package com.opencbs.androidclient.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.opencbs.androidclient.DbHelper;
import com.opencbs.androidclient.model.Branch;

import java.util.List;

import javax.inject.Inject;

public class BranchRepo {

    @Inject
    DbHelper dbHelper;

    @Inject
    public BranchRepo() {}

    public void deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from branches");
    }

    public void add(Branch[] branches) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (Branch branch : branches) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("_id", branch.id);
            contentValues.put("name", branch.name);
            contentValues.put("code", branch.code);
            contentValues.put("description", branch.description);
            contentValues.put("address", branch.address);
            db.insert("branches", null, contentValues);
        }
    }

    public Branch get(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Branch result = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from branches where _id = ?", new String[]{id + ""});
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                result = new Branch();
                result.id = id;
                result.name = cursor.getString(cursor.getColumnIndex("name"));
                result.code = cursor.getString(cursor.getColumnIndex("code"));
                result.description = cursor.getString(cursor.getColumnIndex("description"));
                result.address = cursor.getString(cursor.getColumnIndex("address"));
            }
        } finally {
            cursor.close();
        }
        return result;
    }

    public List<Branch> getAll() {
        return null;
    }
}
