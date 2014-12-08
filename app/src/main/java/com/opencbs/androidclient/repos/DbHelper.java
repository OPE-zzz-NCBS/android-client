package com.opencbs.androidclient.repos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import javax.inject.Inject;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cache.db";
    private static final int DATABASE_VERSION = 1;

    @Inject
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table economic_activities (" +
                        "_id integer primary key, " +
                        "name text not null, " +
                        "parent_id integer not null" +
                        ")"
        );

        db.execSQL(
                "create table branches (" +
                        "_id integer primary key, " +
                        "name text not null, " +
                        "code text not null, " +
                        "description text not null, " +
                        "address text not null" +
                        ")"
        );

        db.execSQL(
                "create table cities (" +
                        "_id integer primary key, " +
                        "name text not null, " +
                        "district_id int not null" +
                        ")"
        );

        db.execSQL(
                "create table districts (" +
                        "_id integer primary key, " +
                        "name text not null, " +
                        "region_id int not null" +
                        ")"
        );

        db.execSQL(
                "create table regions (" +
                        "_id integer primary key, " +
                        "name text not null" +
                        ")"
        );

        db.execSQL(
                "create table custom_fields (" +
                        "_id integer primary key, " +
                        "caption text not null, " +
                        "type text not null, " +
                        "owner text not null, " +
                        "tab text not null, " +
                        "is_unique int not null, " +
                        "required int not null, " +
                        "field_order int not null, " +
                        "extra text not null " +
                        ")"
        );

        db.execSQL(
                "create table people (" +
                        "_id integer primary key, " +
                        "first_name text not null, " +
                        "last_name text not null, " +
                        "father_name text not null, " +
                        "sex text not null, " +
                        "birth_date text not null, " +
                        "birth_place text not null, " +
                        "identification_data text not null, " +
                        "activity_id int not null, " +
                        "branch_id int not null, " +
                        "personal_phone text not null, " +
                        "home_phone text not null, " +
                        "email text not null, " +
                        "city_1_id int not null, " +
                        "address_1 text not null, " +
                        "postal_code_1 text not null, " +
                        "city_2_id int not null, " +
                        "address_2 text not null, " +
                        "postal_code_2 text not null " +
                        ")"
        );

        db.execSQL(
                "create table custom_field_values (" +
                        "field_id int not null, " +
                        "owner_id int not null, " +
                        "value text not null " +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
