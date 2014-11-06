package com.opencbs.androidclient;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;


public class MainActivity extends Activity implements OnEndpointSaveListener, OnLoginListener {

    private enum State {ENDPOINT, LOGIN, DASHBOARD}

    State mState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String endpoint = Settings.getEndpoint(this);
        if (endpoint.isEmpty()) {
            updateState(State.ENDPOINT);
        } else {
            updateState(State.LOGIN);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        switch (mState) {
            case LOGIN:
                getMenuInflater().inflate(R.menu.login_menu, menu);
                return true;

            default:
                return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_login_configure) {
            updateState(State.ENDPOINT);
            return true;
        }

        if (id == android.R.id.home && mState == State.ENDPOINT) {
            updateState(State.LOGIN);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onEndpointSaved() {
        updateState(State.LOGIN);
    }

    @Override
    public void onLogin(String username, String password) {
        Session session = new Session();
        session.username = username;
        session.password = password;

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Settings.getEndpoint(this)).build();
        SessionService sessionService = restAdapter.create(SessionService.class);
        sessionService.login(session, new Callback<Session>() {
            @Override
            public void success(Session session, retrofit.client.Response response) {
                Log.d("OPENCBS", session.token);
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

    private void showEndpointFragment() {
        setTitle(getString(R.string.configure));
        EndpointFragment fragment = new EndpointFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFrameLayout, fragment);
        transaction.commit();
    }

    private void showLoginFragment() {
        setTitle(getString(R.string.login));
        LoginFragment fragment = new LoginFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFrameLayout, fragment);
        transaction.commit();
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void updateState(State state) {
        mState = state;
        switch (mState) {
            case ENDPOINT:
                getActionBar().setDisplayHomeAsUpEnabled(true);
                showEndpointFragment();
                break;

            case LOGIN:
                getActionBar().setDisplayHomeAsUpEnabled(false);
                showLoginFragment();
                break;

            case DASHBOARD:
                break;
        }
        invalidateOptionsMenu();
    }
}
