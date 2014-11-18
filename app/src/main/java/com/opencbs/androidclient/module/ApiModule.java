package com.opencbs.androidclient.module;

import android.content.Context;

import com.opencbs.androidclient.ApiRequestInterceptor;
import com.opencbs.androidclient.api.LookupDataApi;
import com.opencbs.androidclient.api.PersonApi;
import com.opencbs.androidclient.api.SessionApi;
import com.opencbs.androidclient.service.ClientService;
import com.opencbs.androidclient.Settings;
import com.opencbs.androidclient.api.ClientApi;
import com.opencbs.androidclient.service.LookupDataService;
import com.opencbs.androidclient.service.PersonService;
import com.opencbs.androidclient.service.SessionService;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;

@Module(
        injects = {
                ClientService.class,
                SessionService.class,
                PersonService.class,
                LookupDataService.class
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
        return new RestAdapter
                .Builder()
                .setEndpoint(Settings.getEndpoint(context))
                .setRequestInterceptor(new ApiRequestInterceptor(context))
                .build();
    }
}
