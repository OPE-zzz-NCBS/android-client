package com.opencbs.androidclient.fragments;

import android.app.Fragment;

import com.opencbs.androidclient.events.BusEvent;
import com.opencbs.androidclient.events.NoOpEvent;

import java.util.LinkedList;
import java.util.Queue;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class FragmentWithBus extends Fragment {

    protected Queue<BusEvent> eventQueue = new LinkedList<BusEvent>();

    @Inject
    EventBus bus;

    @Override
    public void onResume() {
        super.onResume();
        bus.register(this);
        processEventQueue();
    }

    @Override
    public void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEvent(NoOpEvent event) {
    }

    protected void processEventQueue() {
        while (!eventQueue.isEmpty()) {
            bus.post(eventQueue.remove());
        }
    }

    protected void enqueueEvent(BusEvent event) {
        eventQueue.add(event);
    }
}
