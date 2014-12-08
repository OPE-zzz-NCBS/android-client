package com.opencbs.androidclient.modules;

import android.content.Context;

import com.opencbs.androidclient.App;
import com.opencbs.androidclient.Settings;
import com.opencbs.androidclient.repos.BranchRepo;
import com.opencbs.androidclient.repos.CityRepo;
import com.opencbs.androidclient.repos.ClientRepo;
import com.opencbs.androidclient.repos.CustomFieldRepo;
import com.opencbs.androidclient.repos.DistrictRepo;
import com.opencbs.androidclient.repos.EconomicActivityRepo;
import com.opencbs.androidclient.repos.PersonRepo;
import com.opencbs.androidclient.repos.RegionRepo;
import com.opencbs.androidclient.activities.BranchPickerActivity;
import com.opencbs.androidclient.fragments.ClientsFragment;
import com.opencbs.androidclient.fragments.DownloadFragment;
import com.opencbs.androidclient.activities.EconomicActivityPickerActivity;
import com.opencbs.androidclient.fragments.EndpointFragment;
import com.opencbs.androidclient.activities.LoginActivity;
import com.opencbs.androidclient.fragments.LoginFragment;
import com.opencbs.androidclient.activities.MainActivity;
import com.opencbs.androidclient.activities.PersonActivity;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;

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
                ClientRepo.class
        },
        library = true,
        complete = false
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
    public Settings provideSettings() {
        return new Settings(app);
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

}
