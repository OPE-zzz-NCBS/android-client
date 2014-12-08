package com.opencbs.androidclient.services;

import com.opencbs.androidclient.events.BranchLoadedEvent;
import com.opencbs.androidclient.events.BranchesLoadedEvent;
import com.opencbs.androidclient.events.LoadBranchEvent;
import com.opencbs.androidclient.events.LoadBranchesEvent;
import com.opencbs.androidclient.repos.BranchRepo;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class BranchService {

    @Inject
    EventBus bus;

    @Inject
    BranchRepo branchRepo;

    public void onEvent(LoadBranchEvent event) {
        BranchLoadedEvent responseEvent = new BranchLoadedEvent();
        responseEvent.branch = branchRepo.get(event.branchId);
        responseEvent.actionId = event.actionId;
        bus.post(responseEvent);
    }

    public void onEvent(LoadBranchesEvent event) {
        BranchesLoadedEvent responseEvent = new BranchesLoadedEvent();
        responseEvent.branches = branchRepo.getAll();
        bus.post(responseEvent);
    }
}
