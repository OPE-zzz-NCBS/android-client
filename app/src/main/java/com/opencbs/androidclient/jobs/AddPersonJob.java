package com.opencbs.androidclient.jobs;

import com.opencbs.androidclient.api.PersonApi;
import com.opencbs.androidclient.events.JobStatusChangedEvent;
import com.opencbs.androidclient.models.JobInfo;
import com.opencbs.androidclient.models.Person;
import com.opencbs.androidclient.repos.JobRepo;
import com.opencbs.androidclient.repos.PersonRepo;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class AddPersonJob extends Job {

    @Inject
    PersonApi personApi;

    @Inject
    PersonRepo personRepo;

    @Inject
    JobRepo jobRepo;

    @Inject
    EventBus bus;

    private Person person;
    private JobInfo jobInfo;

    @Inject
    public AddPersonJob() {
        super(new Params(1).requireNetwork());
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public void onAdded() {
        jobInfo = new JobInfo();
        jobInfo.uuid = UUID.randomUUID().toString();
        jobInfo.jobType = "AddPersonJob";
        jobInfo.extra = person.uuid;
        jobInfo.createdAt = new Date();
        jobInfo.description = String.format("%s %s", person.firstName, person.lastName);
        jobInfo.status = JobInfo.STATUS_PENDING;
        jobRepo.add(jobInfo);
    }

    @Override
    public void onRun() throws Throwable {
        Person newPerson = personApi.add(person);
        personRepo.sync(newPerson);
        jobInfo.status = JobInfo.STATUS_DONE;
        jobRepo.updateStatus(jobInfo);
        bus.post(new JobStatusChangedEvent(jobInfo));
    }

    @Override
    protected void onCancel() {
        jobInfo.status = JobInfo.STATUS_FAILED;
        jobRepo.updateStatus(jobInfo);
        bus.post(new JobStatusChangedEvent(jobInfo));
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }
}
