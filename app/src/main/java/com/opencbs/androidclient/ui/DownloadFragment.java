package com.opencbs.androidclient.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.opencbs.androidclient.R;
import com.opencbs.androidclient.event.DownloadLookupDataEvent;
import com.opencbs.androidclient.event.LookupDataDownloadedEvent;

import javax.inject.Inject;

public class DownloadFragment extends FragmentWithBus {

    private Button button;
    private ProgressBar progressBar;

    @Inject
    public DownloadFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download, container, false);

        button = (Button) view.findViewById(R.id.download_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                bus.post(new DownloadLookupDataEvent());
            }
        });

        progressBar = (ProgressBar) view.findViewById(R.id.download_progress_bar);

        return view;
    }

    public void onEvent(LookupDataDownloadedEvent event) {
        progressBar.setVisibility(View.INVISIBLE);
        button.setEnabled(true);
    }
}
