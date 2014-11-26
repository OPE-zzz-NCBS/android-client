package com.opencbs.androidclient.service;

import com.opencbs.androidclient.event.CitiesLoadedEvent;
import com.opencbs.androidclient.event.CityLoadedEvent;
import com.opencbs.androidclient.event.DistrictLoadedEvent;
import com.opencbs.androidclient.event.LoadCitiesEvent;
import com.opencbs.androidclient.event.LoadCityEvent;
import com.opencbs.androidclient.event.LoadDistrictEvent;
import com.opencbs.androidclient.repo.CityRepo;
import com.opencbs.androidclient.repo.DistrictRepo;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class CityService {

    @Inject
    EventBus bus;

    @Inject
    CityRepo cityRepo;

    @Inject
    DistrictRepo districtRepo;

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

    public void onEvent(LoadDistrictEvent event) {
        DistrictLoadedEvent responseEvent = new DistrictLoadedEvent();
        responseEvent.selector = event.selector;
        responseEvent.district = districtRepo.get(event.districtId);
        bus.post(responseEvent);
    }
}
