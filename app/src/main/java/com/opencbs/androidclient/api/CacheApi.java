package com.opencbs.androidclient.api;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Streaming;

public interface CacheApi {
    @GET("/api/cache")
    @Streaming
    Response getCache();
}
