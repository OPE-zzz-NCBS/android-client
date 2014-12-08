package com.opencbs.androidclient.events;

public class LoadClientsEvent extends BusEvent {
    public int offset;
    public int limit;
    public String query;
}
