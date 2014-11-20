package com.opencbs.androidclient.event;

import com.opencbs.androidclient.model.Person;

public class PersonLoadedEvent extends BusEvent {
    public Person person;
}
