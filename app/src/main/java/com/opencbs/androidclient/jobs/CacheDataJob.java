package com.opencbs.androidclient.jobs;

import android.content.Context;
import android.util.Log;

import com.opencbs.androidclient.Settings;
import com.opencbs.androidclient.api.CacheApi;
import com.opencbs.androidclient.events.CacheInitializedEvent;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import retrofit.client.Response;

public class CacheDataJob extends Job {

    @Inject
    Settings settings;

    @Inject
    CacheApi cacheApi;

    @Inject
    EventBus bus;

    @Inject
    Context context;

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

        String dataDir = context.getApplicationInfo().dataDir;
        String fileName = new File(dataDir, "databases").getPath();
        fileName = new File(fileName, "cache.db").getPath();

        Response response = cacheApi.getCache();
        InputStream inputStream = response.getBody().in();
        FileOutputStream outputStream = new FileOutputStream(fileName);
        int bufferSize = 1024 * 100;
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }

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
