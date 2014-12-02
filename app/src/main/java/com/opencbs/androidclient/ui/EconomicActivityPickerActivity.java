package com.opencbs.androidclient.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.opencbs.androidclient.R;
import com.opencbs.androidclient.event.EconomicActivitiesLoadedEvent;
import com.opencbs.androidclient.event.EconomicActivityLoadedEvent;
import com.opencbs.androidclient.event.LoadEconomicActivitiesEvent;
import com.opencbs.androidclient.model.EconomicActivity;

import java.util.ArrayList;

public class EconomicActivityPickerActivity extends OkCancelActivity {

    private ArrayList<EconomicActivity> economicActivities;
    private ArrayAdapter<EconomicActivity> adapter;
    private ListView listView;
    private int economicActivityPickerId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_economic_activity_picker);

        economicActivities = new ArrayList<EconomicActivity>();
        adapter = new ArrayAdapter<EconomicActivity>(this, android.R.layout.simple_list_item_single_choice, economicActivities);
        listView = (ListView) findViewById(R.id.economic_activities_list_view);
        listView.setAdapter(adapter);

        Intent intent = getIntent();
        economicActivityPickerId = intent.getIntExtra("economicActivityPickerId", 0);

        bus.post(new LoadEconomicActivitiesEvent());
    }

    @Override
    protected void onOk() {
        int position = listView.getCheckedItemPosition();
        if (position != ListView.INVALID_POSITION) {
            EconomicActivityLoadedEvent event = new EconomicActivityLoadedEvent();
            event.economicActivity = economicActivities.get(position);
            event.actionId = economicActivityPickerId;
            bus.post(event);
        }
        setResult(Activity.RESULT_OK, null);
        finish();
    }

    @Override
    protected void onCancel() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    public void onEvent(EconomicActivitiesLoadedEvent event) {
        economicActivities.clear();
        economicActivities.addAll(event.economicActivities);
        adapter.notifyDataSetChanged();

        // Select the proper economic activity
        Intent intent = getIntent();
        int id = intent.getIntExtra("economicActivityId", 0);
        if (id > 0) {
            int position = -1;
            for (int j = 0; j < economicActivities.size(); j++) {
                EconomicActivity economicActivity = economicActivities.get(j);
                if (economicActivity.id == id) {
                    position = j;
                    break;
                }
            }
            if (position > -1) {
                listView.setSelection(position);
                listView.setItemChecked(position, true);
            }
        }
    }
}
