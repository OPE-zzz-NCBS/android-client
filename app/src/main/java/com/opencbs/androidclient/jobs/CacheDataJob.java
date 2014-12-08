package com.opencbs.androidclient.jobs;

import android.content.Context;
import android.util.Log;

import com.opencbs.androidclient.Settings;
import com.opencbs.androidclient.api.BranchApi;
import com.opencbs.androidclient.api.CityApi;
import com.opencbs.androidclient.api.CustomFieldApi;
import com.opencbs.androidclient.api.DistrictApi;
import com.opencbs.androidclient.api.EconomicActivityApi;
import com.opencbs.androidclient.api.PersonApi;
import com.opencbs.androidclient.api.RegionApi;
import com.opencbs.androidclient.events.DataCachedEvent;
import com.opencbs.androidclient.model.Person;
import com.opencbs.androidclient.repo.BranchRepo;
import com.opencbs.androidclient.repo.CityRepo;
import com.opencbs.androidclient.repo.CustomFieldRepo;
import com.opencbs.androidclient.repo.DistrictRepo;
import com.opencbs.androidclient.repo.EconomicActivityRepo;
import com.opencbs.androidclient.repo.PersonRepo;
import com.opencbs.androidclient.repo.RegionRepo;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class CacheDataJob extends Job {

    private int offset = 0;
    private static final int LIMIT = 1000;

    @Inject
    Context context;

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
        Settings.setDataState(context, Settings.CACHING);

        int count;
        personRepo.deleteAll();
        do {
            List<Person> people = personApi.getAll(offset, LIMIT);
            personRepo.add(people);
            count = people.size();
            offset += LIMIT;
        } while (count == LIMIT);
        Settings.setDataState(context, Settings.CACHED);

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

        bus.post(new DataCachedEvent());
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
