package com.opencbs.androidclient.jobs;

import android.util.Log;

import com.opencbs.androidclient.api.PersonApi;
import com.opencbs.androidclient.models.Person;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import javax.inject.Inject;

public class PostPersonJob extends Job {

    @Inject
    PersonApi personApi;

    private Person person;

    @Inject
    public PostPersonJob() {
        super(new Params(1).requireNetwork());
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public void onAdded() {
    }

    @Override
    public void onRun() throws Throwable {
        personApi.add(person);
    }

    @Override
    protected void onCancel() {
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        Log.e("ANDROIDCLIENT", throwable.getMessage());
        return false;
    }
}
