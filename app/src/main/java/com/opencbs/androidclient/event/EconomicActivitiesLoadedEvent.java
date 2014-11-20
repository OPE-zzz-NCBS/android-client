package com.opencbs.androidclient.event;

import com.opencbs.androidclient.model.EconomicActivity;

public class EconomicActivitiesLoadedEvent extends BusEvent {
    public EconomicActivity[] economicActivities;
}
