package com.opencbs.androidclient.api;

import com.opencbs.androidclient.Session;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.POST;

public interface SessionApi {
    @POST("/api/sessions")
    void login(@Body Session session, Callback<Session> callback);

    @DELETE("/api/sessions")
    void logout(Callback<?> callback);
}
