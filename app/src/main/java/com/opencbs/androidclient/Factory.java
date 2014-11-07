package com.opencbs.androidclient;

import android.content.Context;

import retrofit.RestAdapter;

public class Factory {

    public static RestAdapter getRestAdapter(Context context) {
        String endpoint = Settings.getEndpoint(context);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(endpoint)
                .setRequestInterceptor(new ApiRequestInterceptor(context))
                .build();
        return restAdapter;
    }
}
