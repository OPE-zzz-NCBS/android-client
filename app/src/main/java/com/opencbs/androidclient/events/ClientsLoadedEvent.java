package com.opencbs.androidclient.events;

import com.opencbs.androidclient.models.Client;

import java.util.List;

public class ClientsLoadedEvent extends BusEvent {
    public List<Client> clients;
}
