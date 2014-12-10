package com.opencbs.androidclient.activities;

import android.app.Activity;
import android.os.Bundle;

import com.opencbs.androidclient.App;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public abstract class BaseActivity extends Activity {

    @Inject
    EventBus bus;

    private boolean visible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((App)getApplication()).inject(this);
        bus.register(this);
    }

    @Override
    protected void onDestroy() {
        bus.unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        visible = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        visible = false;
    }

    protected boolean isVisible() {
        return visible;
    }

    protected <T> T get(Class<T> type) {
        return ((App)getApplication()).get(type);
    }
}
