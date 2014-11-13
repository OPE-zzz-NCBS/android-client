package com.opencbs.androidclient.ui;

import android.app.Activity;
import android.os.Bundle;

import com.opencbs.androidclient.App;

public abstract class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((App)getApplication()).inject(this);
    }

    protected <T> T get(Class<T> type) {
        return ((App)getApplication()).get(type);
    }
}
