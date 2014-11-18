package com.opencbs.androidclient.ui;

import android.app.Activity;
import android.graphics.Color;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.opencbs.androidclient.R;
import com.opencbs.androidclient.event.EconomicActivityLoadedEvent;
import com.opencbs.androidclient.event.LoadEconomicActivityEvent;
import com.opencbs.androidclient.model.Person;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class PersonFormController {

    private Activity activity;

    @Inject
    EventBus bus;

    @Inject
    public PersonFormController() {}

    public void resume() {
        bus.register(this);
    }

    public void pause() {
        bus.unregister(this);
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void onEvent(EconomicActivityLoadedEvent event) {
        LinearLayout ll = (LinearLayout) activity.findViewById(R.id.person_layout);
        TextView textView = (TextView) ll.findViewById(event.actionId);
        if (textView != null && event.economicActivity != null) {
            textView.setText(event.economicActivity.name);
            textView.setTag(event.economicActivity.id);
        }
    }

    public void setPerson(Person person) {
        LinearLayout ll = (LinearLayout) activity.findViewById(R.id.person_layout);
        if (ll.getChildCount() > 0) {
            ll.removeAllViews();
        }

        ll.addView(createLabel("First name"));
        ll.addView(createTextEditor(person.firstName));

        ll.addView(createLabel("Father name"));
        ll.addView(createTextEditor(person.fatherName));

        ll.addView(createLabel("Last name"));
        ll.addView(createTextEditor(person.lastName));

        ll.addView(createLabel("Birth date"));
        ll.addView(createDateEditor(person.birthDate));

        ll.addView(createLabel("Birth place"));
        ll.addView(createTextEditor(person.birthPlace));

        ArrayList<String> list = new ArrayList<String>();
        list.add("Male");
        list.add("Female");
        ll.addView(createLabel("Sex"));
        ll.addView(createSpinner(list, person.sex));

        ll.addView(createLabel("Identification data"));
        ll.addView(createTextEditor(person.identificationData));

        ll.addView(createLabel("Nationality"));
        ll.addView(createTextEditor(person.nationality));

        ll.addView(createLabel("Economic activity"));
        ll.addView(createEconomicActivityPicker(100));
        LoadEconomicActivityEvent leae = new LoadEconomicActivityEvent();
        leae.actionId = 100;
        leae.id = person.activityId;
        bus.post(leae);
    }

     private TextView createLabel(String text) {
        TextView label = new TextView(activity);
        label.setText(text);
        label.setPadding(0, 48, 0, 0);
        label.setTextColor(Color.parseColor("#666666"));
        return label;
    }

    private EditText createTextEditor(String value) {
        EditText editText = new EditText(activity);
        editText.setText(value);
        editText.setSingleLine(true);
        return editText;
    }

    private EditText createDateEditor(Date value) {
        EditText editText = new EditText(activity);
        editText.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
        Format dateFormat = android.text.format.DateFormat.getDateFormat(activity.getApplicationContext());
        String hint = ((SimpleDateFormat) dateFormat).toLocalizedPattern();
        editText.setHint(hint);
        editText.setText(((SimpleDateFormat) dateFormat).format(value));
        return editText;
    }

    private Spinner createSpinner(List<String> items, String value) {
        Spinner spinner = new Spinner(activity);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, items);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        int position = items.indexOf(value);
        if (position > -1) {
            spinner.setSelection(position);
        }
        return spinner;
    }

    private TextView createEconomicActivityPicker(int id) {
        TextView picker = new TextView(activity);
        picker.setText("...");
        picker.setId(id);
        return picker;
    }
}
