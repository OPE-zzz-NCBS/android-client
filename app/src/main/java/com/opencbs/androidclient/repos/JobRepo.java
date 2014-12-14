package com.opencbs.androidclient.repos;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.opencbs.androidclient.models.JobInfo;

import java.text.SimpleDateFormat;

import javax.inject.Inject;

public class JobRepo {

    @Inject
    DbHelper dbHelper;

    @Inject
    public JobRepo() {
    }

    public void add(JobInfo jobInfo) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("uuid", jobInfo.uuid);
        contentValues.put("job_type", jobInfo.jobType);
        contentValues.put("extra", jobInfo.extra);
        contentValues.put("created_at", dateFormat.format(jobInfo.createdAt));
        contentValues.put("description", jobInfo.description);
        contentValues.put("status", jobInfo.status);
        db.insert("jobs", null, contentValues);
    }

    public void updateStatus(JobInfo jobInfo) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", jobInfo.status);
        db.update("jobs", contentValues, "uuid=?", new String[]{jobInfo.uuid});
    }
}
