package com.opencbs.androidclient;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;


public class LoginFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button button = (Button) view.findViewById(R.id.login_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ((EditText) view.findViewById(R.id.username_edit_text)).getText().toString();
                String password = ((EditText) view.findViewById(R.id.password_edit_text)).getText().toString();
                login(username, password);
            }
        });
        return view;
    }

    public void login(String username, String password) {
        Session session = new Session();
        session.username = username;
        session.password = password;

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Settings.getEndpoint(getActivity())).build();
        SessionService sessionService = restAdapter.create(SessionService.class);
        sessionService.login(session, new Callback<Session>() {
            @Override
            public void success(Session session, retrofit.client.Response response) {
                Settings.setAccessToken(getActivity(), session.token);
                LoginListener listener = (LoginListener) getActivity();
                listener.login();
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.getResponse() != null) {
                    if (error.getResponse().getStatus() == 401) {
                        showMessage(getString(R.string.invalid_username_or_password));
                    } else {
                        showMessage(getString(R.string.error_) + error.getResponse().getStatus());
                    }
                } else {
                    showMessage(error.getMessage());
                }
            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}
