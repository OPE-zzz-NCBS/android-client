package com.opencbs.androidclient.event;

public class LoadClientsEvent extends BusEvent {
    public int offset;
    public int limit;
    public String query;
}
