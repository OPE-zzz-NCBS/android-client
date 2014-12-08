package com.opencbs.androidclient.events;

public class LoadPersonEvent extends BusEvent {
    public int id;

    public LoadPersonEvent(int id) {
        this.id = id;
    }
}
