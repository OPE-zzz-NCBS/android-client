package com.opencbs.androidclient.events;

import com.opencbs.androidclient.models.EconomicActivity;

public class EconomicActivityLoadedEvent extends BusEvent {
    public EconomicActivity economicActivity;
    public int actionId;
}
