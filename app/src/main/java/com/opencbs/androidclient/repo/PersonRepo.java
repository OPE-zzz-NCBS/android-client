package com.opencbs.androidclient.repo;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.opencbs.androidclient.model.CustomValue;
import com.opencbs.androidclient.model.Person;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;

public class PersonRepo {

    @Inject
    DbHelper dbHelper;

    public void deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from people");
        db.execSQL(
                "delete from custom_field_values " +
                        "where owner_id in (select _id from people)"
        );
    }

    public void add(List<Person> people) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (Person person : people) {
            try {
                db.beginTransaction();
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", person.id);
                contentValues.put("first_name", person.firstName);
                contentValues.put("last_name", person.lastName);
                contentValues.put("father_name", person.fatherName);
                contentValues.put("sex", person.sex);
                contentValues.put("birth_date", dateFormat.format(person.birthDate));
                contentValues.put("birth_place", person.birthPlace);
                contentValues.put("identification_data", person.identificationData);
                contentValues.put("activity_id", person.activityId);
                contentValues.put("branch_id", person.branchId);
                contentValues.put("personal_phone", person.personalPhone);
                contentValues.put("home_phone", person.homePhone);
                contentValues.put("email", person.email);
                contentValues.put("city_1_id", person.address1.cityId);
                contentValues.put("address_1", person.address1.address);
                contentValues.put("postal_code_1", person.address1.postalCode);
                contentValues.put("city_2_id", person.address2.cityId);
                contentValues.put("address_2", person.address2.address);
                contentValues.put("postal_code_2", person.address2.postalCode);
                db.insert("people", null, contentValues);

                if (person.customInformation != null) {
                    for (CustomValue customValue : person.customInformation) {
                        ContentValues customContentValues = new ContentValues();
                        customContentValues.put("field_id", customValue.field.id);
                        customContentValues.put("owner_id", person.id);
                        customContentValues.put("value", customValue.value);
                    }
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
    }
}
