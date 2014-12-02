package com.opencbs.androidclient.service;

import android.content.Context;

import com.opencbs.androidclient.api.LookupDataApi;
import com.opencbs.androidclient.api.response.LookupDataResponse;
import com.opencbs.androidclient.event.DownloadLookupDataEvent;
import com.opencbs.androidclient.event.LookupDataDownloadedEvent;
import com.opencbs.androidclient.repo.BranchRepo;
import com.opencbs.androidclient.repo.CityRepo;
import com.opencbs.androidclient.repo.CustomFieldRepo;
import com.opencbs.androidclient.repo.DistrictRepo;
import com.opencbs.androidclient.repo.EconomicActivityRepo;
import com.opencbs.androidclient.repo.RegionRepo;

import javax.inject.Inject;
import javax.inject.Provider;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LookupDataService {

    @Inject
    Context context;

    @Inject
    Provider<LookupDataApi> lookupDataApiProvider;

    @Inject
    EventBus bus;

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

    public void onEvent(DownloadLookupDataEvent event) {
        Callback<LookupDataResponse> callback = new Callback<LookupDataResponse>() {
            @Override
            public void success(LookupDataResponse lookupDataResponse, Response response) {
                economicActivityRepo.deleteAll();
                economicActivityRepo.add(lookupDataResponse.economicActivities);

                branchRepo.deleteAll();
                branchRepo.add(lookupDataResponse.branches);

                cityRepo.deleteAll();
                cityRepo.add(lookupDataResponse.cities);

                districtRepo.deleteAll();
                districtRepo.add(lookupDataResponse.districts);

                regionRepo.deleteAll();
                regionRepo.add(lookupDataResponse.regions);

                customFieldRepo.deleteAll();
                customFieldRepo.add(lookupDataResponse.customFields);

                bus.post(new LookupDataDownloadedEvent());
            }

            @Override
            public void failure(RetrofitError error) {

            }
        };
        lookupDataApiProvider.get().get(callback);
    }
}
