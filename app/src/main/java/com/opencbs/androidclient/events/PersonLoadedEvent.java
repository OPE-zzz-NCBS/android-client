package com.opencbs.androidclient.events;

import com.opencbs.androidclient.models.Person;

public class PersonLoadedEvent extends BusEvent {
    public Person person;

    public PersonLoadedEvent(Person person) {
        this.person = person;
    }
}
