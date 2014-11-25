package com.opencbs.androidclient.service;

import android.content.Context;

import com.opencbs.androidclient.DbHelper;
import com.opencbs.androidclient.api.LookupDataApi;
import com.opencbs.androidclient.api.response.LookupDataResponse;
import com.opencbs.androidclient.event.DownloadLookupDataEvent;
import com.opencbs.androidclient.event.LookupDataDownloadedEvent;
import com.opencbs.androidclient.repo.BranchRepo;
import com.opencbs.androidclient.repo.CityRepo;

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
    BranchRepo branchRepo;

    @Inject
    CityRepo cityRepo;

    public void onEvent(DownloadLookupDataEvent event) {
        Callback<LookupDataResponse> callback = new Callback<LookupDataResponse>() {
            @Override
            public void success(LookupDataResponse lookupDataResponse, Response response) {
                DbHelper dbHelper = new DbHelper(context);
                dbHelper.deleteEconomicActivities();
                dbHelper.addEconomicActivities(lookupDataResponse.economicActivities);

                branchRepo.deleteAll();
                branchRepo.add(lookupDataResponse.branches);

                cityRepo.deleteAll();
                cityRepo.add(lookupDataResponse.cities);

                bus.post(new LookupDataDownloadedEvent());
            }

            @Override
            public void failure(RetrofitError error) {

            }
        };
        lookupDataApiProvider.get().get(callback);
    }
}
