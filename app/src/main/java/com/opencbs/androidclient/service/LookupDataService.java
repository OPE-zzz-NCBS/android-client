package com.opencbs.androidclient.service;

import com.opencbs.androidclient.api.LookupDataApi;
import com.opencbs.androidclient.api.response.LookupDataResponse;
import com.opencbs.androidclient.event.DownloadLookupDataEvent;
import com.opencbs.androidclient.event.LookupDataDownloadedEvent;

import javax.inject.Inject;
import javax.inject.Provider;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LookupDataService {

    @Inject
    Provider<LookupDataApi> lookupDataApiProvider;

    @Inject
    EventBus bus;

    public void onEvent(DownloadLookupDataEvent event) {
        Callback<LookupDataResponse> callback = new Callback<LookupDataResponse>() {
            @Override
            public void success(LookupDataResponse lookupDataResponse, Response response) {
                bus.post(new LookupDataDownloadedEvent());
            }

            @Override
            public void failure(RetrofitError error) {

            }
        };
        lookupDataApiProvider.get().get(callback);
    }
}
