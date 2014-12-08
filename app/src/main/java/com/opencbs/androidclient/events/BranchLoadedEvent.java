package com.opencbs.androidclient.events;

import com.opencbs.androidclient.models.Branch;

public class BranchLoadedEvent extends BusEvent {
    public Branch branch;
    public int actionId;
}
