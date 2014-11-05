package com.opencbs.androidclient;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by Pavel on 11/5/2014.
 */
public interface SessionService {
    @POST("/api/sessions")
    void login(@Body Session session, Callback<Session> callback);
}
