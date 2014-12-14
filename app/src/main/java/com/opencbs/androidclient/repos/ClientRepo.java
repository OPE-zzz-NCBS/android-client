package com.opencbs.androidclient.repos;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.opencbs.androidclient.models.Client;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ClientRepo {

    @Inject
    DbHelper dbHelper;

    public List<Client> getAll(int offset, int limit) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<Client> result = new ArrayList<>();
        Cursor cursor = null;
        try {
            String query = "select id, uuid, last_name || ', ' || first_name name " +
                    "from people " +
                    "order by last_name || ', ' || first_name " +
                    "limit " + limit + " offset " + offset;
            cursor = db.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Client client = new Client();
                    client.id = cursor.getInt(cursor.getColumnIndex("id"));
                    client.uuid = cursor.getString(cursor.getColumnIndex("uuid"));
                    client.name = cursor.getString(cursor.getColumnIndex("name"));
                    client.type = "PERSON";
                    result.add(client);
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

    public List<Client> search(String searchString, int offset, int limit) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<Client> result = new ArrayList<>();
        Cursor cursor = null;
        try {
            String query = "select id, uuid, last_name || ', ' || first_name name " +
                    "from people " +
                    "where first_name || ' ' || last_name like ?" +
                    "order by last_name || ', ' || first_name " +
                    "limit " + limit + " offset " + offset;
            cursor = db.rawQuery(query, new String[]{"%" + searchString + "%"});
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Client client = new Client();
                    client.id = cursor.getInt(cursor.getColumnIndex("id"));
                    client.uuid = cursor.getString(cursor.getColumnIndex("uuid"));
                    client.name = cursor.getString(cursor.getColumnIndex("name"));
                    client.type = "PERSON";
                    result.add(client);
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
