package com.opencbs.androidclient;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.POST;

public interface SessionService {
    @POST("/api/sessions")
    void login(@Body Session session, Callback<Session> callback);

    @DELETE("/api/sessions")
    void logout(Callback<?> callback);
}
