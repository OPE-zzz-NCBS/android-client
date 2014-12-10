package com.opencbs.androidclient.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.opencbs.androidclient.R;
import com.opencbs.androidclient.Settings;
import com.opencbs.androidclient.jobs.CacheDataJob;
import com.path.android.jobqueue.JobManager;

import javax.inject.Inject;
import javax.inject.Provider;

public class DownloadFragment extends FragmentWithBus {

    private Button button;
    private ProgressBar progressBar;

    @Inject
    JobManager jobManager;

    @Inject
    Provider<CacheDataJob> cacheDataJobProvider;

    @Inject
    Settings settings;

    @Inject
    public DownloadFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download, container, false);

        button = (Button) view.findViewById(R.id.download_button);
        progressBar = (ProgressBar) view.findViewById(R.id.download_progress_bar);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                jobManager.addJobInBackground(cacheDataJobProvider.get());
            }
        });

        Settings.CacheState cacheState = settings.getCacheState();

        if (cacheState == Settings.CacheState.INITIALIZING) {
            button.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
        }

        return view;
    }
}
