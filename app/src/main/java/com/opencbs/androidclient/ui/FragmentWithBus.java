package com.opencbs.androidclient.ui;

import android.app.Fragment;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class FragmentWithBus extends Fragment {

    @Inject
    EventBus bus;

    @Override
    public void onResume() {
        super.onResume();
        bus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        bus.unregister(this);
    }
}
