package com.opencbs.androidclient.service;

import com.opencbs.androidclient.ClientsResponse;
import com.opencbs.androidclient.api.ClientApi;
import com.opencbs.androidclient.event.ClientsLoadedEvent;
import com.opencbs.androidclient.event.LoadClientsEvent;

import javax.inject.Inject;
import javax.inject.Provider;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ClientService {

    @Inject
    Provider<ClientApi> clientApi;

    @Inject
    EventBus bus;

    public void onEvent(LoadClientsEvent event) {
        Callback<ClientsResponse> callback = new Callback<ClientsResponse>() {
            @Override
            public void success(ClientsResponse clientsResponse, Response response) {
                ClientsLoadedEvent event = new ClientsLoadedEvent();
                event.offset = clientsResponse.offset;
                event.limit = clientsResponse.limit;
                event.count = clientsResponse.count;
                event.clients = clientsResponse.items;
                bus.post(event);
            }

            @Override
            public void failure(RetrofitError error) {
            }
        };

        if (event.query.isEmpty()) {
            clientApi.get().getAll(event.offset, event.limit, event.includeCount, callback);
        } else {
            clientApi.get().search(event.offset, event.limit, event.includeCount, event.query, callback);
        }
    }
}
