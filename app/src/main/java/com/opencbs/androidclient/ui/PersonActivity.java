package com.opencbs.androidclient.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.opencbs.androidclient.R;
import com.opencbs.androidclient.event.LoadPersonEvent;
import com.opencbs.androidclient.event.PersonLoadedEvent;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class PersonActivity extends BaseActivity {

    @Inject
    EventBus bus;

    @Inject
    PersonFormController controller;

    private int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        controller.setActivity(this);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_person, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bus.register(this);
        controller.resume();
        loadPerson();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bus.unregister(this);
        controller.pause();
    }

    private void loadPerson() {
        LoadPersonEvent event = new LoadPersonEvent();
        event.id = id;
        bus.post(event);
    }

    public void onEvent(PersonLoadedEvent event) {
        controller.setPerson(event.person);
    }
}
