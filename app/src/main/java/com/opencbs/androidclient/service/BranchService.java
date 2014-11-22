package com.opencbs.androidclient.service;

import com.opencbs.androidclient.event.BranchLoadedEvent;
import com.opencbs.androidclient.event.BranchesLoadedEvent;
import com.opencbs.androidclient.event.LoadBranchEvent;
import com.opencbs.androidclient.event.LoadBranchesEvent;
import com.opencbs.androidclient.repo.BranchRepo;

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
