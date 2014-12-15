package com.opencbs.androidclient.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.opencbs.androidclient.JobArrayAdapter;
import com.opencbs.androidclient.R;
import com.opencbs.androidclient.events.JobStatusChangedEvent;
import com.opencbs.androidclient.events.JobsLoadedEvent;
import com.opencbs.androidclient.events.LoadJobsEvent;
import com.opencbs.androidclient.models.JobInfo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class JobsFragment extends FragmentWithBus {

    private JobArrayAdapter adapter;
    private List<JobInfo> jobs;

    @Inject
    public JobsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jobs, container, false);

        jobs = new ArrayList<>();
        adapter = new JobArrayAdapter(getActivity(), jobs);
        ListView listView = (ListView) view.findViewById(R.id.jobs_list_view);
        listView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        bus.post(new LoadJobsEvent());
    }

    public void onEventMainThread(JobsLoadedEvent event) {
        jobs.clear();
        jobs.addAll(event.getJobs());
        adapter.notifyDataSetChanged();
    }

    public void onEventMainThread(JobStatusChangedEvent event) {
        JobInfo found = null;
        for (JobInfo jobInfo : jobs) {
            if (jobInfo.equals(event.getJobInfo())) {
                found = jobInfo;
                break;
            }
        }
        if (found == null) return;
        found.status = event.getJobInfo().status;
        adapter.notifyDataSetChanged();
    }
}
