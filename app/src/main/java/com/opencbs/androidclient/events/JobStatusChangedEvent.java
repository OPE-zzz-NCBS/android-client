package com.opencbs.androidclient.events;

import com.opencbs.androidclient.models.JobInfo;

public class JobStatusChangedEvent extends BusEvent {
    private JobInfo jobInfo;

    public JobStatusChangedEvent(JobInfo jobInfo) {
        this.jobInfo = jobInfo;
    }

    public JobInfo getJobInfo() {
        return this.jobInfo;
    }
}
