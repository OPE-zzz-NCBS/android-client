package com.opencbs.androidclient.events;

import com.opencbs.androidclient.models.City;

import java.util.List;

public class CitiesLoadedEvent extends BusEvent {
    public int selector;
    public List<City> cities;
}
