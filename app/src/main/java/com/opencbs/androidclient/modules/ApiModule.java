package com.opencbs.androidclient.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencbs.androidclient.api.ApiRequestInterceptor;
import com.opencbs.androidclient.api.BranchApi;
import com.opencbs.androidclient.api.CityApi;
import com.opencbs.androidclient.api.CustomFieldApi;
import com.opencbs.androidclient.api.DistrictApi;
import com.opencbs.androidclient.api.EconomicActivityApi;
import com.opencbs.androidclient.api.PersonApi;
import com.opencbs.androidclient.api.RegionApi;
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
import retrofit.converter.GsonConverter;

@Module(
        injects = {
                ClientService.class,
                SessionService.class,
                PersonService.class,
                EconomicActivityService.class,
                BranchService.class,
                CityService.class,
                CustomFieldService.class
        },
        library = true,
        complete = false
)
public class ApiModule {

    @Provides
    public SessionApi provideSessionApi(Settings settings) {
        return getRestAdapter(settings).create(SessionApi.class);
    }

    @Provides
    public PersonApi providePersonApi(Settings settings) {
        return getRestAdapter(settings).create(PersonApi.class);
    }

    @Provides
    public EconomicActivityApi provideEconomicActivityApi(Settings settings) {
        return getRestAdapter(settings).create(EconomicActivityApi.class);
    }

    @Provides
    public BranchApi provideBranchApi(Settings settings) {
        return getRestAdapter(settings).create(BranchApi.class);
    }

    @Provides
    public CityApi provideCityApi(Settings settings) {
        return getRestAdapter(settings).create(CityApi.class);
    }

    @Provides
    public DistrictApi provideDistrictApi(Settings settings) {
        return getRestAdapter(settings).create(DistrictApi.class);
    }

    @Provides
    public RegionApi provideRegionApi(Settings settings) {
        return getRestAdapter(settings).create(RegionApi.class);
    }

    @Provides
    public CustomFieldApi provideCustomFieldApi(Settings settings) {
        return getRestAdapter(settings).create(CustomFieldApi.class);
    }

    private RestAdapter getRestAdapter(Settings settings) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        return new RestAdapter
                .Builder()
                .setEndpoint(settings.getEndpoint())
                .setRequestInterceptor(new ApiRequestInterceptor(settings))
                .setConverter(new GsonConverter(gson))
                .build();
    }
}
