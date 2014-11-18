package com.opencbs.androidclient.service;

import android.util.Log;

import com.opencbs.androidclient.model.Person;
import com.opencbs.androidclient.api.PersonApi;
import com.opencbs.androidclient.event.LoadPersonEvent;
import com.opencbs.androidclient.event.PersonLoadedEvent;

import javax.inject.Inject;
import javax.inject.Provider;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PersonService {

    @Inject
    Provider<PersonApi> personApiProvider;

    @Inject
    EventBus bus;

    public void onEvent(LoadPersonEvent event) {
        Callback<Person> callback = new Callback<Person>() {
            @Override
            public void success(Person person, Response response) {
                PersonLoadedEvent event = new PersonLoadedEvent();
                event.person = person;
                bus.post(event);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("ANDROIDCLIENT", error.getMessage());
            }
        };
        personApiProvider.get().getById(event.id, callback);
    }
}
