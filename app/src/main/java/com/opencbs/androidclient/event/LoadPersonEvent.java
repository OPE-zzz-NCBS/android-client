package com.opencbs.androidclient.event;

public class LoadPersonEvent extends BusEvent {
    public int id;

    public LoadPersonEvent(int id) {
        this.id = id;
    }
}
