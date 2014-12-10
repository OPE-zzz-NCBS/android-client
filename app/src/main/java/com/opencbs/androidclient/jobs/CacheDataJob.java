package com.opencbs.androidclient.jobs;

import android.util.Log;

import com.opencbs.androidclient.Settings;
import com.opencbs.androidclient.api.BranchApi;
import com.opencbs.androidclient.api.CityApi;
import com.opencbs.androidclient.api.CustomFieldApi;
import com.opencbs.androidclient.api.DistrictApi;
import com.opencbs.androidclient.api.EconomicActivityApi;
import com.opencbs.androidclient.api.PersonApi;
import com.opencbs.androidclient.api.RegionApi;
import com.opencbs.androidclient.events.CacheInitializedEvent;
import com.opencbs.androidclient.models.Person;
import com.opencbs.androidclient.repos.BranchRepo;
import com.opencbs.androidclient.repos.CityRepo;
import com.opencbs.androidclient.repos.CustomFieldRepo;
import com.opencbs.androidclient.repos.DistrictRepo;
import com.opencbs.androidclient.repos.EconomicActivityRepo;
import com.opencbs.androidclient.repos.PersonRepo;
import com.opencbs.androidclient.repos.RegionRepo;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class CacheDataJob extends Job {

    private int offset = 0;
    private static final int LIMIT = 1000;

    @Inject
    Settings settings;

    @Inject
    PersonApi personApi;

    @Inject
    EconomicActivityApi economicActivityApi;

    @Inject
    BranchApi branchApi;

    @Inject
    CityApi cityApi;

    @Inject
    DistrictApi districtApi;

    @Inject
    RegionApi regionApi;

    @Inject
    CustomFieldApi customFieldApi;

    @Inject
    PersonRepo personRepo;

    @Inject
    EconomicActivityRepo economicActivityRepo;

    @Inject
    BranchRepo branchRepo;

    @Inject
    CityRepo cityRepo;

    @Inject
    DistrictRepo districtRepo;

    @Inject
    RegionRepo regionRepo;

    @Inject
    CustomFieldRepo customFieldRepo;

    @Inject
    EventBus bus;

    @Inject
    public CacheDataJob() {
        super(new Params(1).requireNetwork());
    }

    @Override
    public void onAdded() {
    }

    @Override
    public void onRun() throws Throwable {
        settings.setCacheState(Settings.CacheState.INITIALIZING);

        int count;
        personRepo.deleteAll();
        do {
            List<Person> people = personApi.getAll(offset, LIMIT);
            personRepo.add(people);
            count = people.size();
            offset += LIMIT;
        } while (count == LIMIT);

        economicActivityRepo.deleteAll();
        economicActivityRepo.add(economicActivityApi.getAll());

        branchRepo.deleteAll();
        branchRepo.add(branchApi.getAll());

        cityRepo.deleteAll();
        cityRepo.add(cityApi.getAll());

        districtRepo.deleteAll();
        districtRepo.add(districtApi.getAll());

        regionRepo.deleteAll();
        regionRepo.add(regionApi.getAll());

        customFieldRepo.deleteAll();
        customFieldRepo.add(customFieldApi.getAll());

        settings.setCacheState(Settings.CacheState.INITIALIZED);

        bus.post(new CacheInitializedEvent());
    }

    @Override
    protected void onCancel() {
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        Log.e("ANDROIDCLIENT", throwable.getMessage());
        return false;
    }
}
