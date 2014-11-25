package com.opencbs.androidclient.service;

import com.opencbs.androidclient.event.CitiesLoadedEvent;
import com.opencbs.androidclient.event.CityLoadedEvent;
import com.opencbs.androidclient.event.LoadCitiesEvent;
import com.opencbs.androidclient.event.LoadCityEvent;
import com.opencbs.androidclient.repo.CityRepo;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class CityService {

    @Inject
    EventBus bus;

    @Inject
    CityRepo cityRepo;

    public void onEvent(LoadCitiesEvent event) {
        CitiesLoadedEvent responseEvent = new CitiesLoadedEvent();
        responseEvent.selector = event.selector;
        responseEvent.cities = cityRepo.getAll();
        bus.post(responseEvent);
    }

    public void onEvent(LoadCityEvent event) {
        CityLoadedEvent responseEvent = new CityLoadedEvent();
        responseEvent.selector = event.selector;
        responseEvent.city = cityRepo.get(event.cityId);
        bus.post(responseEvent);
    }
}
