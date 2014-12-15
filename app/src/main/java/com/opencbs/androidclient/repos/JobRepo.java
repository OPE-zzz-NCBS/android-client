package com.opencbs.androidclient.repos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.opencbs.androidclient.models.JobInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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

    public List<JobInfo> getAll() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<JobInfo> jobs = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from jobs order by created_at desc", null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    JobInfo job = new JobInfo();
                    job.uuid = cursor.getString(cursor.getColumnIndex("uuid"));
                    job.jobType = cursor.getString(cursor.getColumnIndex("job_type"));
                    job.extra = cursor.getString(cursor.getColumnIndex("extra"));
                    job.description = cursor.getString(cursor.getColumnIndex("description"));
                    job.status = cursor.getInt(cursor.getColumnIndex("status"));
                    job.createdAt = dateFormat.parse(cursor.getString(cursor.getColumnIndex("created_at")));
                    jobs.add(job);
                    cursor.moveToNext();
                }
            }
        } catch (ParseException ignored) {
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return jobs;
    }
}
