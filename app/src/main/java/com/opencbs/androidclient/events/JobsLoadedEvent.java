package com.opencbs.androidclient.events;

import com.opencbs.androidclient.models.JobInfo;

import java.util.List;

public class JobsLoadedEvent extends BusEvent {
    private List<JobInfo> jobs;

    public JobsLoadedEvent(List<JobInfo> jobs) {
        this.jobs = jobs;
    }

    public List<JobInfo> getJobs() {
        return jobs;
    }
}
