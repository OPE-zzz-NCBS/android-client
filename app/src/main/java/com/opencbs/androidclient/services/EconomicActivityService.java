package com.opencbs.androidclient.services;

import com.opencbs.androidclient.events.EconomicActivitiesLoadedEvent;
import com.opencbs.androidclient.events.EconomicActivityLoadedEvent;
import com.opencbs.androidclient.events.LoadEconomicActivitiesEvent;
import com.opencbs.androidclient.events.LoadEconomicActivityEvent;
import com.opencbs.androidclient.repos.EconomicActivityRepo;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class EconomicActivityService {

    @Inject
    EventBus bus;

    @Inject
    EconomicActivityRepo economicActivityRepo;

    public void onEvent(LoadEconomicActivityEvent event) {
        EconomicActivityLoadedEvent responseEvent = new EconomicActivityLoadedEvent();
        responseEvent.economicActivity = economicActivityRepo.get(event.economicActivityId);
        responseEvent.actionId = event.actionId;
        bus.post(responseEvent);
    }

    public void onEvent(LoadEconomicActivitiesEvent event) {
        EconomicActivitiesLoadedEvent responseEvent = new EconomicActivitiesLoadedEvent();
        responseEvent.economicActivities = economicActivityRepo.getAll();
        bus.post(responseEvent);
    }
}
