package com.opencbs.androidclient;

import android.app.Application;

import com.opencbs.androidclient.module.ApiModule;
import com.opencbs.androidclient.module.AppModule;
import com.opencbs.androidclient.service.BranchService;
import com.opencbs.androidclient.service.CityService;
import com.opencbs.androidclient.service.ClientService;
import com.opencbs.androidclient.service.CustomFieldService;
import com.opencbs.androidclient.service.EconomicActivityService;
import com.opencbs.androidclient.service.LookupDataService;
import com.opencbs.androidclient.service.PersonService;
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
    private PersonService personService;
    private LookupDataService lookupDataService;
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

        lookupDataService = get(LookupDataService.class);
        bus.register(lookupDataService);

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
        bus.unregister(lookupDataService);
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
                new AppModule(this),
                new ApiModule(this)
        );
    }
}
