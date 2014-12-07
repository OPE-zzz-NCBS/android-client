package com.opencbs.androidclient.api;

import com.opencbs.androidclient.model.Region;

import java.util.List;

import retrofit.http.GET;

public interface RegionApi {
    @GET("/api/regions")
    List<Region> getAll();
}
