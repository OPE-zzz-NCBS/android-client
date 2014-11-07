package com.opencbs.androidclient;

import android.content.Context;

import retrofit.RequestInterceptor;

public class ApiRequestInterceptor implements RequestInterceptor {

    private Context mContext;

    public ApiRequestInterceptor(Context context) {
        mContext = context;
    }

    @Override
    public void intercept(RequestFacade request) {
        String accessToken = Settings.getAccessToken(mContext);
        if (accessToken.isEmpty()) return;
        request.addHeader("x-access-token", accessToken);
    }
}
