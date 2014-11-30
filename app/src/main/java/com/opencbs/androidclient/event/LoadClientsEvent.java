package com.opencbs.androidclient.event;

import com.opencbs.androidclient.model.ClientRange;

public class LoadClientsEvent extends BusEvent {
    public String query;
    public ClientRange clientRange;
}
