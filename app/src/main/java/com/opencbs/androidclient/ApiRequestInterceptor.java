package com.opencbs.androidclient;

import retrofit.RequestInterceptor;

public class ApiRequestInterceptor implements RequestInterceptor {

    private Settings settings;

    public ApiRequestInterceptor(Settings settings) {
        this.settings = settings;
    }

    @Override
    public void intercept(RequestFacade request) {
        String accessToken = settings.getAccessToken();
        if (accessToken.isEmpty()) return;
        request.addHeader("x-access-token", accessToken);
    }
}
