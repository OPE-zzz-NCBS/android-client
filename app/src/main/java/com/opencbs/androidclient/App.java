package com.opencbs.androidclient;

import android.app.Application;

import com.opencbs.androidclient.module.AppModule;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;

public class App extends Application {
    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        objectGraph = ObjectGraph.create(getModules().toArray());
        objectGraph.inject(this);
    }

    public void inject(Object object) {
        objectGraph.inject(object);
    }

    public <T> T get(Class<T> type) {
        return objectGraph.get(type);
    }

    protected List<Object> getModules() {
        return Arrays.<Object>asList(
                new AppModule(this)
        );
    }
}
