package com.opencbs.androidclient.service;

import android.util.Log;

import com.opencbs.androidclient.model.Client;
import com.opencbs.androidclient.api.ClientApi;
import com.opencbs.androidclient.event.ClientsLoadedEvent;
import com.opencbs.androidclient.event.LoadClientsEvent;
import com.opencbs.androidclient.model.ClientRange;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Provider;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.client.Header;

public class ClientService {

    @Inject
    Provider<ClientApi> clientApi;

    @Inject
    EventBus bus;

    public void onEvent(final LoadClientsEvent event) {
        Callback<List<Client>> callback = new Callback<List<Client>>() {
            @Override
            public void success(List<Client> clients, Response response) {
                ClientsLoadedEvent responseEvent = new ClientsLoadedEvent();
                responseEvent.clients = clients;
                responseEvent.thisRange = event.clientRange;

                for (Header header : response.getHeaders()) {
                    if (header.getName() == null) continue;
                    if (header.getName().equals("Content-Range")) {
                        String patternString =  "clients (\\d+)\\.\\.(\\d+)/(\\d+)";
                        Pattern pattern = Pattern.compile(patternString);
                        Matcher matcher = pattern.matcher(header.getValue());
                        if (!matcher.matches()) break;

                        int from = Integer.parseInt(matcher.group(1));
                        int to = Integer.parseInt(matcher.group(2));
                        int count = Integer.parseInt(matcher.group(3));

                        if (to < count - 1) {
                            int size = to - from;
                            from = to + 1;
                            to = from + size;
                            if (to >= count - 1) to = count - 1;
                            responseEvent.nextRange = new ClientRange(from, to);
                        }
                        break;
                    }
                }
                bus.post(responseEvent);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("ANDROIDCLIENT", error.getMessage());
            }
        };

        if (event.query.isEmpty()) {
            clientApi.get().getAll(event.clientRange, callback);
        } else {
            clientApi.get().search(event.query, event.clientRange, callback);
        }
    }
}
