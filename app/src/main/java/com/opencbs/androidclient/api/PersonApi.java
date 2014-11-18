package com.opencbs.androidclient.api;

import com.opencbs.androidclient.model.Person;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

public interface PersonApi {
    @GET("/api/people/{id}")
    void getById(@Path("id") int id, Callback<Person> callback);
}
