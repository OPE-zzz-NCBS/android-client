package com.opencbs.androidclient;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity implements OnEndpointSaveListener {

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
