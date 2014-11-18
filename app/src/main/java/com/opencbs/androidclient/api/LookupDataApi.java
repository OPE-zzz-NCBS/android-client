package com.opencbs.androidclient.api;

import com.opencbs.androidclient.api.response.LookupDataResponse;

import retrofit.Callback;
import retrofit.http.GET;

public interface LookupDataApi {
    @GET("/api/lookupdata")
    void get(Callback<LookupDataResponse> callback);
}
