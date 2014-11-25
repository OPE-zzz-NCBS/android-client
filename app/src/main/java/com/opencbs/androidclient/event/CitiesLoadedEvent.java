package com.opencbs.androidclient.event;

import com.opencbs.androidclient.model.City;

public class CitiesLoadedEvent extends BusEvent {
    public int selector;
    public City[] cities;
}
