package com.opencbs.androidclient;

import android.app.Application;

import com.opencbs.androidclient.modules.ApiModule;
import com.opencbs.androidclient.modules.AppModule;
import com.opencbs.androidclient.services.BranchService;
import com.opencbs.androidclient.services.CityService;
import com.opencbs.androidclient.services.ClientService;
import com.opencbs.androidclient.services.CustomFieldService;
import com.opencbs.androidclient.services.EconomicActivityService;
import com.opencbs.androidclient.services.PersonService;
import com.opencbs.androidclient.services.SessionService;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;
import de.greenrobot.event.EventBus;

public class App extends Application {
    private ObjectGraph objectGraph;

    private ClientService clientService;
    private SessionService sessionService;
    private PersonService personService;
    private EconomicActivityService economicActivityService;
    private BranchService branchService;
    private CityService cityService;
    private CustomFieldService customFieldService;

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

        personService = get(PersonService.class);
        bus.register(personService);

        economicActivityService = get(EconomicActivityService.class);
        bus.register(economicActivityService);

        branchService = get(BranchService.class);
        bus.register(branchService);

        cityService = get(CityService.class);
        bus.register(cityService);

        customFieldService = get(CustomFieldService.class);
        bus.register(customFieldService);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        bus.unregister(clientService);
        bus.unregister(sessionService);
        bus.unregister(personService);
        bus.unregister(economicActivityService);
        bus.unregister(branchService);
        bus.unregister(cityService);
        bus.unregister(customFieldService);
    }

    public void inject(Object object) {
        objectGraph.inject(object);
    }

    public <T> T get(Class<T> type) {
        return objectGraph.get(type);
    }

    protected List<Object> getModules() {
        return Arrays.asList(
                new ApiModule(this),
                new AppModule(this)
        );
    }
}
