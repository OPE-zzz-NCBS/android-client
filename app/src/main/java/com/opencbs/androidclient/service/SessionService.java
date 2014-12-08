package com.opencbs.androidclient.service;

import com.opencbs.androidclient.Session;
import com.opencbs.androidclient.api.SessionApi;
import com.opencbs.androidclient.events.LoginEvent;
import com.opencbs.androidclient.events.LoginFailureEvent;
import com.opencbs.androidclient.events.LoginSuccessEvent;
import com.opencbs.androidclient.events.LogoutEvent;
import com.opencbs.androidclient.events.LogoutSuccessEvent;

import javax.inject.Inject;
import javax.inject.Provider;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SessionService {

    @Inject
    Provider<SessionApi> sessionApiProvider;

    @Inject
    EventBus bus;

    public void onEvent(LoginEvent event) {
        Session session = new Session();
        session.username = event.username;
        session.password = event.password;

        sessionApiProvider.get().login(session, new Callback<Session>() {

            @Override
            public void success(Session session, Response response) {
                LoginSuccessEvent successEvent = new LoginSuccessEvent();
                successEvent.session = session;
                bus.post(successEvent);
            }

            @Override
            public void failure(RetrofitError error) {
                LoginFailureEvent failureEvent = new LoginFailureEvent();
                if (error.getResponse() != null) {
                    if (error.getResponse().getStatus() == 401) {
                        failureEvent.error = "Invlaid username or password.";
                    } else {
                        failureEvent.error = "Error: " +error.getResponse().getStatus();
                    }
                } else {
                    failureEvent.error = error.getMessage();
                }
                bus.post(failureEvent);
            }
        });
    }

    public void onEvent(LogoutEvent event) {
        sessionApiProvider.get().logout(new Callback<Object>() {
            @Override
            public void success(Object o, Response response) {
                bus.post(new LogoutSuccessEvent());
            }

            @Override
            public void failure(RetrofitError error) {
                bus.post(new LogoutSuccessEvent());
            }
        });
    }
}
