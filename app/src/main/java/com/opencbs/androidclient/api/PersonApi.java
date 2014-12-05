package com.opencbs.androidclient.api;

import com.opencbs.androidclient.model.Person;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface PersonApi {
    @GET("/api/people/{id}")
    void getById(@Path("id") int id, Callback<Person> callback);

    @POST("/api/people")
    void add(@Body Person person, Callback<Person> callback);

    @GET("/api/people")
    List<Person> getAll(@Query("offset") int offset, @Query("limit") int limit);
}
