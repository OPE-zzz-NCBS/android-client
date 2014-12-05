package com.opencbs.androidclient.api;

import com.opencbs.androidclient.model.Client;
import com.opencbs.androidclient.model.ClientRange;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Query;

public interface ClientApi {
    @GET("/api/clients")
    void getAll(@Header("Range") ClientRange clientRange, Callback<List<Client>> callback);

    @GET("/api/clients")
    void search(@Query("query") String query, @Header("Range") ClientRange clientRange, Callback<List<Client>> callback);
}
