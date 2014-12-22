package com.opencbs.androidclient;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.opencbs.androidclient.models.JobInfo;

import java.text.SimpleDateFormat;
import java.util.List;

public class JobArrayAdapter extends ArrayAdapter<JobInfo> {

    private Context context;
    private List<JobInfo> jobs;

    public JobArrayAdapter(Context context, List<JobInfo> jobs) {
        super(context, R.layout.layout_job_row, jobs);
        this.context = context;
        this.jobs = jobs;
    }

    private String getName(JobInfo job) {
        switch (job.jobType) {
            case "AddPersonJob":
                return context.getString(R.string.add_person);
        }
        return "Unknown";
    }

    private int getStatusColor(JobInfo job) {
        switch (job.status) {
            case JobInfo.STATUS_DONE:
                return context.getResources().getColor(R.color.status_done);

            case JobInfo.STATUS_FAILED:
                return context.getResources().getColor(R.color.status_failed);

            case JobInfo.STATUS_PENDING:
                return context.getResources().getColor(R.color.status_pending);
        }
        return 0;
    }

    private String getStatusText(JobInfo job) {
        switch (job.status) {
            case JobInfo.STATUS_DONE:
                return context.getString(R.string.synced).toUpperCase();

            case JobInfo.STATUS_FAILED:
                return context.getString(R.string.failed).toUpperCase();

            case JobInfo.STATUS_PENDING:
                return context.getString(R.string.pending).toUpperCase();
        }
        return "";
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.layout_job_row, parent, false);
        JobInfo jobInfo = jobs.get(position);
        TextView nameTextView = (TextView) rowView.findViewById(R.id.job_name_text_view);
        nameTextView.setText(getName(jobInfo));

        TextView descriptionTextView = (TextView) rowView.findViewById(R.id.job_description_text_view);
        descriptionTextView.setText(jobInfo.description);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TextView createdAtTextView = (TextView) rowView.findViewById(R.id.job_created_at_text_view);
        createdAtTextView.setText(dateFormat.format(jobInfo.createdAt));

        TextView statusTextView = (TextView) rowView.findViewById(R.id.job_status_text_view);
        statusTextView.setText(getStatusText(jobInfo));
        statusTextView.setTextColor(getStatusColor(jobInfo));

        return rowView;
    }
}
