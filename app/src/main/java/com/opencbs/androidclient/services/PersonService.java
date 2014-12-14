package com.opencbs.androidclient.services;

import com.opencbs.androidclient.events.PersonLoadedEvent;
import com.opencbs.androidclient.models.Person;
import com.opencbs.androidclient.events.LoadPersonEvent;
import com.opencbs.androidclient.repos.PersonRepo;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class PersonService {

    @Inject
    EventBus bus;

    @Inject
    PersonRepo personRepo;

    public void onEvent(LoadPersonEvent event) {
        Person person = personRepo.get(event.uuid);
        PersonLoadedEvent responseEvent = new PersonLoadedEvent(person);
        bus.post(responseEvent);
    }
}
