package com.opencbs.androidclient.event;

import com.opencbs.androidclient.model.EconomicActivity;

public class EconomicActivityLoadedEvent extends BusEvent {
    public EconomicActivity economicActivity;
    public int actionId;
}
