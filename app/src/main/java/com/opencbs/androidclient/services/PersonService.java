package com.opencbs.androidclient.services;

import com.opencbs.androidclient.events.AddPersonEvent;
import com.opencbs.androidclient.events.PersonLoadedEvent;
import com.opencbs.androidclient.jobs.AddPersonJob;
import com.opencbs.androidclient.models.Person;
import com.opencbs.androidclient.events.LoadPersonEvent;
import com.opencbs.androidclient.repos.PersonRepo;
import com.path.android.jobqueue.JobManager;

import javax.inject.Inject;
import javax.inject.Provider;

import de.greenrobot.event.EventBus;

public class PersonService {

    @Inject
    EventBus bus;

    @Inject
    PersonRepo personRepo;

    @Inject
    JobManager jobManager;

    @Inject
    Provider<AddPersonJob> postPersonJobProvider;

    public void onEvent(LoadPersonEvent event) {
        Person person = personRepo.get(event.uuid);
        PersonLoadedEvent responseEvent = new PersonLoadedEvent(person);
        bus.post(responseEvent);
    }

    public void onEvent(AddPersonEvent event) {
        personRepo.add(event.getPerson());

        AddPersonJob job = postPersonJobProvider.get();
        job.setPerson(event.getPerson());
        jobManager.addJobInBackground(job);
    }
}
