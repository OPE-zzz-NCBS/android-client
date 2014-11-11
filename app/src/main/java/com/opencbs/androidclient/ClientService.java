package com.opencbs.androidclient;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface ClientService {
    @GET("/api/clients")
    void getAll(@Query("offset") int offset,
                @Query("limit") int limit,
                @Query("includeCount") boolean includeCount,
                Callback<ClientsResponse> callback);

    @GET("/api/clients")
    void search(@Query("offset") int offset,
                @Query("limit") int limit,
                @Query("includeCount") boolean includeCount,
                @Query("query") String query,
                Callback<ClientsResponse> callback);
}
