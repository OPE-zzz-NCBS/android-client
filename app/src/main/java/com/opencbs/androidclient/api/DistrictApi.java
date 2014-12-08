package com.opencbs.androidclient.api;

import com.opencbs.androidclient.models.District;

import java.util.List;

import retrofit.http.GET;

public interface DistrictApi {
    @GET("/api/districts")
    List<District> getAll();
}
