package com.opencbs.androidclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.opencbs.androidclient.models.JobInfo;

import java.util.List;

public class JobArrayAdapter extends ArrayAdapter<JobInfo> {

    private Context context;
    private List<JobInfo> jobs;

    public JobArrayAdapter(Context context, List<JobInfo> jobs) {
        super(context, R.layout.layout_job_row, jobs);
        this.context = context;
        this.jobs = jobs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.layout_job_row, parent, false);
        JobInfo jobInfo = jobs.get(position);
        TextView nameTextView = (TextView) rowView.findViewById(R.id.job_name_text_view);
        nameTextView.setText(jobInfo.jobType);
        return rowView;
    }
}
