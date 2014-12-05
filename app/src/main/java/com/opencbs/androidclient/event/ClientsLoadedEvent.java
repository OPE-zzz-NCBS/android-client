package com.opencbs.androidclient.event;

import com.opencbs.androidclient.model.Client;
import com.opencbs.androidclient.model.ClientRange;

import java.util.List;

public class ClientsLoadedEvent extends BusEvent {
    public List<Client> clients;
    public ClientRange thisRange;
    public ClientRange nextRange;
}
