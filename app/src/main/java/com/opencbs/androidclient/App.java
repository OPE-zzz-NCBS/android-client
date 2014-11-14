package com.opencbs.androidclient;

import android.app.Application;

import com.opencbs.androidclient.module.ApiModule;
import com.opencbs.androidclient.module.AppModule;
import com.opencbs.androidclient.service.ClientService;
import com.opencbs.androidclient.service.SessionService;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;
import de.greenrobot.event.EventBus;

public class App extends Application {
    private ObjectGraph objectGraph;

    private ClientService clientService;
    private SessionService sessionService;

    @Inject
    EventBus bus;

    @Override
    public void onCreate() {
        super.onCreate();
        objectGraph = ObjectGraph.create(getModules().toArray());
        objectGraph.inject(this);

        clientService = get(ClientService.class);
        bus.register(clientService);

        sessionService = get(SessionService.class);
        bus.register(sessionService);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        bus.unregister(clientService);
        bus.unregister(sessionService);
    }

    public void inject(Object object) {
        objectGraph.inject(object);
    }

    public <T> T get(Class<T> type) {
        return objectGraph.get(type);
    }

    protected List<Object> getModules() {
        return Arrays.asList(
                new AppModule(this),
                new ApiModule(this)
        );
    }
}
