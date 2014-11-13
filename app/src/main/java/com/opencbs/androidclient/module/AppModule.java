package com.opencbs.androidclient.module;

import com.opencbs.androidclient.App;
import com.opencbs.androidclient.ui.ClientsFragment;
import com.opencbs.androidclient.ui.LoginActivity;
import com.opencbs.androidclient.ui.MainActivity;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = {
                App.class,
                LoginActivity.class,
                MainActivity.class,
                ClientsFragment.class
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
    public Bus provideBus() {
        return new Bus();
    }
}
