package com.opencbs.androidclient.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.opencbs.androidclient.R;

import javax.inject.Inject;

public class DownloadFragment extends FragmentWithBus {

    @Inject
    public DownloadFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download, container, false);
        return view;
    }
}
