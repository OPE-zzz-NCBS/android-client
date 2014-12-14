package com.opencbs.androidclient.events;

import com.opencbs.androidclient.models.Person;

public class AddPersonEvent extends BusEvent {
    private Person person;

    public AddPersonEvent(Person person) {
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }
}
