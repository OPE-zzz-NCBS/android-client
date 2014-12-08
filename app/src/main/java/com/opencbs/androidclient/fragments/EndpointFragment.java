package com.opencbs.androidclient.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.opencbs.androidclient.EndpointListener;
import com.opencbs.androidclient.R;
import com.opencbs.androidclient.Settings;

import javax.inject.Inject;

public class EndpointFragment extends Fragment {

    @Inject
    public EndpointFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_endpoint, container, false);

        Button button = (Button) view.findViewById(R.id.endpoint_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText endpointEditText = (EditText) view.findViewById(R.id.endpoint_edit_text);
                String endpoint = endpointEditText.getText().toString();
                Settings.setEndpoint(getActivity(), endpoint);

                EndpointListener listener = (EndpointListener) getActivity();
                listener.saveEndpoint();
            }
        });

        String endpoint = Settings.getEndpoint(getActivity());
        EditText endpointEditText = (EditText) view.findViewById(R.id.endpoint_edit_text);
        endpointEditText.setText(endpoint);

        return view;
    }
}
