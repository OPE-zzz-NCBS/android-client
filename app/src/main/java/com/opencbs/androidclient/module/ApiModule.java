package com.opencbs.androidclient.module;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencbs.androidclient.ApiRequestInterceptor;
import com.opencbs.androidclient.api.LookupDataApi;
import com.opencbs.androidclient.api.PersonApi;
import com.opencbs.androidclient.api.SessionApi;
import com.opencbs.androidclient.service.BranchService;
import com.opencbs.androidclient.service.CityService;
import com.opencbs.androidclient.service.ClientService;
import com.opencbs.androidclient.Settings;
import com.opencbs.androidclient.api.ClientApi;
import com.opencbs.androidclient.service.CustomFieldService;
import com.opencbs.androidclient.service.EconomicActivityService;
import com.opencbs.androidclient.service.LookupDataService;
import com.opencbs.androidclient.service.PersonService;
import com.opencbs.androidclient.service.SessionService;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

@Module(
        injects = {
                ClientService.class,
                SessionService.class,
                PersonService.class,
                LookupDataService.class,
                EconomicActivityService.class,
                BranchService.class,
                CityService.class,
                CustomFieldService.class,
        },
        library = true,
        complete = false
)
public class ApiModule {
    private Context context;

    public ApiModule(Context context) {
        this.context = context;
    }

    @Provides
    public ClientApi provideClientApi() {
        return getRestAdapter().create(ClientApi.class);
    }

    @Provides
    public SessionApi provideSessionApi() {
        return getRestAdapter().create(SessionApi.class);
    }

    @Provides
    public PersonApi providePersonApi() {
        return getRestAdapter().create(PersonApi.class);
    }

    @Provides
    public LookupDataApi provideLookupDataApi() {
        return getRestAdapter().create(LookupDataApi.class);
    }

    private RestAdapter getRestAdapter() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        return new RestAdapter
                .Builder()
                .setEndpoint(Settings.getEndpoint(context))
                .setRequestInterceptor(new ApiRequestInterceptor(context))
                .setConverter(new GsonConverter(gson))
                .build();
    }
}
