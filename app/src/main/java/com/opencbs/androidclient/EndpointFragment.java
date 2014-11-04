package com.opencbs.androidclient;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class EndpointFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_endpoint, container, false);

        Button button = (Button) view.findViewById(R.id.saveAddressButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnEndpointSaveListener listener = (OnEndpointSaveListener) getActivity();
                listener.onEndpointSaved();
            }
        });

        return view;
    }
}
