package com.opencbs.androidclient.module;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencbs.androidclient.ApiRequestInterceptor;
import com.opencbs.androidclient.App;
import com.opencbs.androidclient.Settings;
import com.opencbs.androidclient.api.PersonApi;
import com.opencbs.androidclient.repo.BranchRepo;
import com.opencbs.androidclient.repo.CityRepo;
import com.opencbs.androidclient.repo.ClientRepo;
import com.opencbs.androidclient.repo.CustomFieldRepo;
import com.opencbs.androidclient.repo.DistrictRepo;
import com.opencbs.androidclient.repo.EconomicActivityRepo;
import com.opencbs.androidclient.repo.PersonRepo;
import com.opencbs.androidclient.repo.RegionRepo;
import com.opencbs.androidclient.ui.BranchPickerActivity;
import com.opencbs.androidclient.ui.ClientsFragment;
import com.opencbs.androidclient.ui.DownloadFragment;
import com.opencbs.androidclient.ui.EconomicActivityPickerActivity;
import com.opencbs.androidclient.ui.EndpointFragment;
import com.opencbs.androidclient.ui.LoginActivity;
import com.opencbs.androidclient.ui.LoginFragment;
import com.opencbs.androidclient.ui.MainActivity;
import com.opencbs.androidclient.ui.PersonActivity;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

@Module(
        injects = {
                App.class,
                LoginActivity.class,
                MainActivity.class,
                ClientsFragment.class,
                LoginFragment.class,
                DownloadFragment.class,
                EndpointFragment.class,
                PersonActivity.class,
                EconomicActivityPickerActivity.class,
                BranchPickerActivity.class,

                BranchRepo.class,
                CityRepo.class,
                DistrictRepo.class,
                RegionRepo.class,
                EconomicActivityRepo.class,
                CustomFieldRepo.class,
                PersonRepo.class,
                ClientRepo.class,
        },
        library = true
)
public class AppModule {
    private App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    public Context provideApplicationContext() {
        return app;
    }

    @Provides
    @Singleton
    public EventBus provideBus() {
        return new EventBus();
    }

    @Provides
    @Singleton
    public JobManager provideJobManager() {
        Configuration configuration = new Configuration.Builder(app)
                .minConsumerCount(1)
                .maxConsumerCount(3)
                .loadFactor(3)
                .consumerKeepAlive(120)
                .build();
        return new JobManager(app, configuration);
    }

    @Provides
    public PersonApi providePersonApi() {
        return getRestAdapter().create(PersonApi.class);
    }

    private RestAdapter getRestAdapter() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        return new RestAdapter
                .Builder()
                .setEndpoint(Settings.getEndpoint(app))
                .setRequestInterceptor(new ApiRequestInterceptor(app))
                .setConverter(new GsonConverter(gson))
                .build();
    }
}
