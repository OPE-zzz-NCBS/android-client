package com.opencbs.androidclient.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.opencbs.androidclient.R;
import com.opencbs.androidclient.event.BusEvent;
import com.opencbs.androidclient.event.EconomicActivityLoadedEvent;
import com.opencbs.androidclient.event.LoadEconomicActivityEvent;
import com.opencbs.androidclient.event.LoadPersonEvent;
import com.opencbs.androidclient.event.PersonLoadedEvent;
import com.opencbs.androidclient.model.Person;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class PersonActivity extends BaseActivity {

    private static final int FIRST_NAME_ID = 1;
    private static final int LAST_NAME_ID = 2;
    private static final int FATHER_NAME_ID = 3;
    private static final int BIRTH_DATE_ID = 4;
    private static final int BIRTH_PLACE_ID = 5;
    private static final int SEX_ID = 6;
    private static final int IDENTIFICATION_DATA_ID = 7;
    private static final int NATIONALITY_ID = 8;
    private static final int ECONOMIC_ACTIVITY_ID = 9;

    private static final int PICK_ECONOMIC_ACTIVITY_REQUEST = 1;

    private Queue<BusEvent> eventQueue = new LinkedList<BusEvent>();

    @Inject
    EventBus bus;

    private int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);

        LoadPersonEvent event = new LoadPersonEvent();
        event.id = id;
        eventQueue.add(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bus.register(this);
        processEventQueue();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_ECONOMIC_ACTIVITY_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                LoadEconomicActivityEvent event = new LoadEconomicActivityEvent();
                event.actionId = data.getIntExtra("economicActivityPickerId", 0);
                event.economicActivityId = data.getIntExtra("economicActivityId", 0);
                eventQueue.add(event);
            }
        }
    }

    private void processEventQueue() {
        while (!eventQueue.isEmpty()) {
            bus.post(eventQueue.remove());
        }
    }

    public void onEvent(PersonLoadedEvent event) {
        setPerson(event.person);
    }

    public void onEvent(EconomicActivityLoadedEvent event) {
        LinearLayout ll = (LinearLayout) findViewById(R.id.person_layout);
        Button button = (Button) ll.findViewById(event.actionId);
        if (button != null && event.economicActivity != null) {
            button.setText(event.economicActivity.name);
            button.setTag(event.economicActivity.id);
        }
    }

    private void setPerson(Person person) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.person_layout);
        if (layout.getChildCount() > 0) {
            layout.removeAllViews();
        }

        addLabel(layout, "First name");
        addTextEditor(layout, FIRST_NAME_ID, person.firstName);

        addLabel(layout, "Father name");
        addTextEditor(layout, FATHER_NAME_ID, person.fatherName);

        addLabel(layout, "Last name");
        addTextEditor(layout, LAST_NAME_ID, person.lastName);

        addLabel(layout, "Birth date");
        addDateEditor(layout, BIRTH_DATE_ID, person.birthDate);

        addLabel(layout, "Birth place");
        addTextEditor(layout, BIRTH_PLACE_ID, person.birthPlace);

        addLabel(layout, "Sex");
        ArrayList<String> list = new ArrayList<String>();
        list.add("Male");
        list.add("Female");
        addSpinner(layout, SEX_ID, list, person.sex);

        addLabel(layout, "Identification data");
        addTextEditor(layout, IDENTIFICATION_DATA_ID, person.identificationData);

        addLabel(layout, "Nationality");
        addTextEditor(layout, NATIONALITY_ID, person.nationality);

        addLabel(layout, "Economic activity");
        addEconomicActivityPicker(layout, ECONOMIC_ACTIVITY_ID, person.activityId);
    }

    private void addLabel(ViewGroup group, String text) {
        TextView label = new TextView(this);
        label.setText(text);
        label.setPadding(0, 48, 0, 0);
        label.setTextColor(Color.parseColor("#666666"));
        group.addView(label);
    }

    private void addTextEditor(ViewGroup group, int id, String value) {
        EditText editText = new EditText(this);
        editText.setText(value);
        editText.setSingleLine(true);
        editText.setId(id);
        group.addView(editText);
    }

    private void addDateEditor(ViewGroup group, int id, Date value) {
        EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
        Format dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        String hint = ((SimpleDateFormat) dateFormat).toLocalizedPattern();
        editText.setHint(hint);
        editText.setText(((SimpleDateFormat) dateFormat).format(value));
        editText.setId(id);
        group.addView(editText);
    }

    private void addSpinner(ViewGroup group, int id, List<String> items, String value) {
        Spinner spinner = new Spinner(this);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        int position = items.indexOf(value);
        if (position > -1) {
            spinner.setSelection(position);
        }
        spinner.setId(id);
        group.addView(spinner);
    }

    private void addEconomicActivityPicker(ViewGroup group, final int id, final int economicActivityId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        final Button button = (Button) inflater.inflate(R.layout.spinner_button, group, false);
        button.setText("");
        button.setId(id);
        button.setTag(economicActivityId);
        group.addView(button);

        final Context context = this;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EconomicActivityPickerActivity.class);
                intent.putExtra("economicActivityPickerId", id);
                intent.putExtra("economicActivityId", (Integer) button.getTag());
                startActivityForResult(intent, PICK_ECONOMIC_ACTIVITY_REQUEST);
            }
        });

        LoadEconomicActivityEvent event = new LoadEconomicActivityEvent();
        event.actionId = id;
        event.economicActivityId = economicActivityId;
        bus.post(event);
    }
}
