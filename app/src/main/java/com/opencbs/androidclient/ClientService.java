package com.opencbs.androidclient;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface ClientService {
    @GET("/api/clients")
    void getClients(@Query("offset") int offset, @Query("limit") int limit, Callback<ClientsResponse> callback);
}
