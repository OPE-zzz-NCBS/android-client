package com.opencbs.androidclient.ui;

import android.os.Bundle;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public abstract class ActivityWithBus extends BaseActivity {

    @Inject
    EventBus bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bus.register(this);
    }

    @Override
    protected void onDestroy() {
        bus.unregister(this);
        super.onDestroy();
    }
}
