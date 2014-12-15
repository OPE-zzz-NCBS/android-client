package com.opencbs.androidclient.services;

import com.opencbs.androidclient.events.JobsLoadedEvent;
import com.opencbs.androidclient.events.LoadJobsEvent;
import com.opencbs.androidclient.repos.JobRepo;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class JobService {

    @Inject
    JobRepo jobRepo;

    @Inject
    EventBus bus;

    @SuppressWarnings("UnusedDeclaration")
    public void onEventBackgroundThread(LoadJobsEvent event) {
        JobsLoadedEvent responseEvent = new JobsLoadedEvent(jobRepo.getAll());
        bus.post(responseEvent);
    }
}
