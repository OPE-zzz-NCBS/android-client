package com.opencbs.androidclient.service;

import com.opencbs.androidclient.DbHelper;
import com.opencbs.androidclient.event.EconomicActivitiesLoadedEvent;
import com.opencbs.androidclient.event.EconomicActivityLoadedEvent;
import com.opencbs.androidclient.event.LoadEconomicActivitiesEvent;
import com.opencbs.androidclient.event.LoadEconomicActivityEvent;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class EconomicActivityService {

    @Inject
    EventBus bus;

    @Inject
    DbHelper dbHelper;

    public void onEvent(LoadEconomicActivityEvent event) {
        EconomicActivityLoadedEvent responseEvent = new EconomicActivityLoadedEvent();
        responseEvent.economicActivity = dbHelper.getEconomicActivity(event.economicActivityId);
        responseEvent.actionId = event.actionId;
        bus.post(responseEvent);
    }

    public void onEvent(LoadEconomicActivitiesEvent event) {
        EconomicActivitiesLoadedEvent responseEvent = new EconomicActivitiesLoadedEvent();
        responseEvent.economicActivities = dbHelper.getEconomicActivities();
        bus.post(responseEvent);
    }
}
