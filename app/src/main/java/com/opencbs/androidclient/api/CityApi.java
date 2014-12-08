package com.opencbs.androidclient.api;

import com.opencbs.androidclient.models.City;

import java.util.List;

import retrofit.http.GET;

public interface CityApi {
    @GET("/api/cities")
    List<City> getAll();
}
