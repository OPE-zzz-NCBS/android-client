package com.opencbs.androidclient.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.opencbs.androidclient.R;
import com.opencbs.androidclient.event.EconomicActivitiesLoadedEvent;
import com.opencbs.androidclient.event.LoadEconomicActivitiesEvent;
import com.opencbs.androidclient.model.EconomicActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class EconomicActivityPickerActivity extends OkCancelActivity {

    private ArrayList<EconomicActivity> economicActivities;
    private ArrayAdapter<EconomicActivity> adapter;

    @Inject
    EventBus bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_economic_activity_picker);

        economicActivities = new ArrayList<EconomicActivity>();
        adapter = new ArrayAdapter<EconomicActivity>(this, android.R.layout.simple_list_item_1, economicActivities);
        ListView listView = (ListView) findViewById(R.id.economic_activities_list_view);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bus.register(this);
        loadEconomicActivities();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    @Override
    protected void onOk() {
    }

    @Override
    protected void onCancel() {
    }

    public void onEvent(EconomicActivitiesLoadedEvent event) {
        economicActivities.clear();
        Collections.addAll(economicActivities, event.economicActivities);
        adapter.notifyDataSetChanged();
    }

    private void loadEconomicActivities() {
        LoadEconomicActivitiesEvent event = new LoadEconomicActivitiesEvent();
        bus.post(event);
    }
}
