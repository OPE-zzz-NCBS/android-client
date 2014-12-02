package com.opencbs.androidclient.module;

import android.content.Context;

import com.opencbs.androidclient.App;
import com.opencbs.androidclient.repo.BranchRepo;
import com.opencbs.androidclient.repo.CityRepo;
import com.opencbs.androidclient.repo.DistrictRepo;
import com.opencbs.androidclient.repo.EconomicActivityRepo;
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
}
