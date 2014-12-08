package com.opencbs.androidclient.api;

import com.opencbs.androidclient.models.EconomicActivity;

import java.util.List;

import retrofit.http.GET;

public interface EconomicActivityApi {
    @GET("/api/economic-activities")
    public List<EconomicActivity> getAll();
}
