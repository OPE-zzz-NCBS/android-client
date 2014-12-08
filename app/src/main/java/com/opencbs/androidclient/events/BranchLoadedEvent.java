package com.opencbs.androidclient.events;

import com.opencbs.androidclient.model.Branch;

public class BranchLoadedEvent extends BusEvent {
    public Branch branch;
    public int actionId;
}
