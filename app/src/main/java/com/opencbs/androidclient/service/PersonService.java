package com.opencbs.androidclient.service;

import com.opencbs.androidclient.event.PersonLoadedEvent;
import com.opencbs.androidclient.model.Person;
import com.opencbs.androidclient.event.LoadPersonEvent;
import com.opencbs.androidclient.repo.PersonRepo;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class PersonService {

    @Inject
    EventBus bus;

    @Inject
    PersonRepo personRepo;

    public void onEvent(LoadPersonEvent event) {
        Person person = personRepo.get(event.id);
        PersonLoadedEvent responseEvent = new PersonLoadedEvent(person);
        bus.post(responseEvent);
    }
}
