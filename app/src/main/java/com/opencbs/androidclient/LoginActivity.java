package com.opencbs.androidclient;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class LoginActivity extends Activity implements LoginListener, EndpointListener {

    private enum State {ENDPOINT, LOGIN}

    State mState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String accessToken = Settings.getAccessToken(this);
        if (!accessToken.isEmpty()) {
            openMainActivity();
        } else {
            String endpoint = Settings.getEndpoint(this);
            if (endpoint.isEmpty()) {
                setState(State.ENDPOINT);
            } else {
                setState(State.LOGIN);
            }
            setContentView(R.layout.activity_login);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mState == State.LOGIN) {
            getMenuInflater().inflate(R.menu.menu_login, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_login_configure) {
            setState(State.ENDPOINT);
            return true;
        }

        if (id == android.R.id.home && mState == State.ENDPOINT) {
            setState(State.LOGIN);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void login() {
        openMainActivity();
    }

    @Override
    public void saveEndpoint() {
        setState(State.LOGIN);
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showEndpointFragment() {
        setTitle(getString(R.string.configure));
        EndpointFragment fragment = new EndpointFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.login_frame_layout, fragment);
        transaction.commit();
    }

    private void showLoginFragment() {
        setTitle(getString(R.string.login));
        LoginFragment fragment = new LoginFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.login_frame_layout, fragment);
        transaction.commit();
    }

    private void setState(State state) {
        ActionBar actionBar = getActionBar();
        mState = state;
        switch (mState) {
            case ENDPOINT:
                if (actionBar != null) {
                    actionBar.setDisplayHomeAsUpEnabled(true);
                }
                showEndpointFragment();
                break;

            case LOGIN:
                if (actionBar != null) {
                    actionBar.setDisplayHomeAsUpEnabled(false);
                    actionBar.setHomeButtonEnabled(false);
                }
                showLoginFragment();
                break;
        }
        invalidateOptionsMenu();
    }
}
