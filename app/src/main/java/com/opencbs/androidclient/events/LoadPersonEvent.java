package com.opencbs.androidclient.events;

public class LoadPersonEvent extends BusEvent {
    public String uuid;

    public LoadPersonEvent(String uuid) {
        this.uuid = uuid;
    }
}
