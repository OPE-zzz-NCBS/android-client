package com.opencbs.androidclient;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;


public class LoginFragment extends Fragment {

    private Button mLoginButton;
    private ProgressBar mProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_login, container, false);

        mLoginButton = (Button) view.findViewById(R.id.login_button);
        mProgressBar = (ProgressBar) view.findViewById(R.id.login_progress_bar);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
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
        startLogin();

        Session session = new Session();
        session.username = username;
        session.password = password;

        RestAdapter restAdapter = Factory.getRestAdapter(getActivity());
        SessionService sessionService = restAdapter.create(SessionService.class);
        sessionService.login(session, new Callback<Session>() {
            @Override
            public void success(Session session, retrofit.client.Response response) {
                stopLogin();
                Settings.setAccessToken(getActivity(), session.token);
                LoginListener listener = (LoginListener) getActivity();
                listener.login();
            }

            @Override
            public void failure(RetrofitError error) {
                stopLogin();
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

    private void startLogin() {
        mProgressBar.setVisibility(View.VISIBLE);
        mLoginButton.setEnabled(false);
    }

    private void stopLogin() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mLoginButton.setEnabled(true);
    }
}
