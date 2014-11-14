package com.opencbs.androidclient.ui;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.opencbs.androidclient.R;
import com.opencbs.androidclient.event.LoginEvent;
import com.opencbs.androidclient.event.LoginFailureEvent;
import com.opencbs.androidclient.event.LoginSuccessEvent;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;


public class LoginFragment extends Fragment {

    private Button mLoginButton;
    private ProgressBar mProgressBar;

    @Inject
    EventBus bus;

    @Inject
    public LoginFragment() {}

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

    @Override
    public void onResume() {
        super.onResume();
        bus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    public void onEvent(LoginSuccessEvent event) {
        stopLogin();
    }

    public void onEvent(LoginFailureEvent event) {
        stopLogin();
        showMessage(event.error);
    }

    public void login(String username, String password) {
        startLogin();
        LoginEvent event = new LoginEvent();
        event.username = username;
        event.password = password;
        bus.post(event);
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
