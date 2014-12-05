package com.opencbs.androidclient.jobs;

import android.content.Context;
import android.util.Log;

import com.opencbs.androidclient.Settings;
import com.opencbs.androidclient.api.PersonApi;
import com.opencbs.androidclient.event.DataCachedEvent;
import com.opencbs.androidclient.model.Person;
import com.opencbs.androidclient.repo.PersonRepo;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import de.greenrobot.event.EventBus;

public class CacheDataJob extends Job {

    private int offset = 0;
    private static final int LIMIT = 1000;

    @Inject
    Context context;

    @Inject
    Provider<PersonApi> personApiProvider;

    @Inject
    PersonRepo personRepo;

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
            List<Person> people = personApiProvider.get().getAll(offset, LIMIT);
            personRepo.add(people);
            count = people.size();
            offset += LIMIT;
        } while (count == LIMIT);
        Settings.setDataState(context, Settings.CACHED);
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
