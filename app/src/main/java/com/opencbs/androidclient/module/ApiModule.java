package com.opencbs.androidclient.module;

import android.content.Context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencbs.androidclient.ApiRequestInterceptor;
import com.opencbs.androidclient.JacksonConverter;
import com.opencbs.androidclient.api.SessionApi;
import com.opencbs.androidclient.services.BranchService;
import com.opencbs.androidclient.services.CityService;
import com.opencbs.androidclient.services.ClientService;
import com.opencbs.androidclient.Settings;
import com.opencbs.androidclient.services.CustomFieldService;
import com.opencbs.androidclient.services.EconomicActivityService;
import com.opencbs.androidclient.services.PersonService;
import com.opencbs.androidclient.services.SessionService;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;

@Module(
        injects = {
                ClientService.class,
                SessionService.class,
                PersonService.class,
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
    public SessionApi provideSessionApi() {
        return getRestAdapter().create(SessionApi.class);
    }

    private RestAdapter getRestAdapter() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        JacksonConverter converter = new JacksonConverter(new ObjectMapper());

        return new RestAdapter
                .Builder()
                .setEndpoint(Settings.getEndpoint(context))
                .setRequestInterceptor(new ApiRequestInterceptor(context))
                //.setConverter(new GsonConverter(gson))
                .setConverter(converter)
                .build();
    }
}
