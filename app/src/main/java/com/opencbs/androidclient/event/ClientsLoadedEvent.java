package com.opencbs.androidclient.event;

import com.opencbs.androidclient.Client;

public class ClientsLoadedEvent {
    public int offset;
    public int limit;
    public int count;
    public Client[] clients;
}