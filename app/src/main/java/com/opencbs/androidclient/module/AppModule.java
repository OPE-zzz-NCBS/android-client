package com.opencbs.androidclient.module;

import android.content.Context;

import com.opencbs.androidclient.App;
import com.opencbs.androidclient.ui.ClientsFragment;
import com.opencbs.androidclient.ui.LoginActivity;
import com.opencbs.androidclient.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;

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
    public Context provideApplicationContext() {
        return app;
    }

    @Provides
    @Singleton
    public EventBus provideBus() {
        return new EventBus();
    }
}
