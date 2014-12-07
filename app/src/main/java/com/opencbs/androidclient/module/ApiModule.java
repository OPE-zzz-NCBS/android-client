package com.opencbs.androidclient.module;

import android.content.Context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencbs.androidclient.ApiRequestInterceptor;
import com.opencbs.androidclient.JacksonConverter;
import com.opencbs.androidclient.api.SessionApi;
import com.opencbs.androidclient.service.BranchService;
import com.opencbs.androidclient.service.CityService;
import com.opencbs.androidclient.service.ClientService;
import com.opencbs.androidclient.Settings;
import com.opencbs.androidclient.service.CustomFieldService;
import com.opencbs.androidclient.service.EconomicActivityService;
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
