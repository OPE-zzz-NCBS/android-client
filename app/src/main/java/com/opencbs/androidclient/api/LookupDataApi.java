package com.opencbs.androidclient.api;

import com.opencbs.androidclient.api.response.LookupDataResponse;

import retrofit.Callback;
import retrofit.http.GET;

public interface LookupDataApi {
    @GET("/api/lookup-data")
    void get(Callback<LookupDataResponse> callback);
}
