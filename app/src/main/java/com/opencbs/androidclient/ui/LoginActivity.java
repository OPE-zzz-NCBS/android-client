package com.opencbs.androidclient.ui;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.opencbs.androidclient.EndpointListener;
import com.opencbs.androidclient.R;
import com.opencbs.androidclient.Settings;
import com.opencbs.androidclient.event.LoginSuccessEvent;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class LoginActivity extends BaseActivity implements EndpointListener {

    private enum State {ENDPOINT, LOGIN}

    State state;

    @Inject
    LoginFragment loginFragment;

    @Inject
    EndpointFragment endpointFragment;

    @Inject
    EventBus bus;

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
        if (state == State.LOGIN) {
            getMenuInflater().inflate(R.menu.menu_login, menu);
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        bus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_login_configure) {
            setState(State.ENDPOINT);
            return true;
        }

        if (id == android.R.id.home && state == State.ENDPOINT) {
            setState(State.LOGIN);
        }

        return super.onOptionsItemSelected(item);
    }

    public void onEvent(LoginSuccessEvent event) {
        Settings.setAccessToken(this, event.session.token);
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
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.login_frame_layout, endpointFragment);
        transaction.commit();
    }

    private void showLoginFragment() {
        setTitle(getString(R.string.login));
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.login_frame_layout, loginFragment);
        transaction.commit();
    }

    private void setState(State state) {
        ActionBar actionBar = getActionBar();
        this.state = state;
        switch (this.state) {
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
