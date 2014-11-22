package com.opencbs.androidclient.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.opencbs.androidclient.R;
import com.opencbs.androidclient.event.BranchLoadedEvent;
import com.opencbs.androidclient.event.EconomicActivityLoadedEvent;
import com.opencbs.androidclient.event.LoadBranchEvent;
import com.opencbs.androidclient.event.LoadEconomicActivityEvent;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public abstract class EditorActivity extends ActivityWithBus {

    private static final int PICK_ECONOMIC_ACTIVITY_REQUEST = 1;
    private static final int PICK_BRANCH_REQUEST = 2;

    public void onEvent(EconomicActivityLoadedEvent event) {
        ViewGroup viewGroup = getContainer();
        Button button = (Button) viewGroup.findViewById(event.actionId);
        if (button != null && event.economicActivity != null) {
            button.setText(event.economicActivity.name);
            button.setTag(event.economicActivity.id);
        }
    }

    protected abstract ViewGroup getContainer();

    protected void addLabel(String text) {
        TextView label = new TextView(this);
        label.setText(text);
        label.setPadding(0, 48, 0, 0);
        label.setTextColor(Color.parseColor("#666666"));
        getContainer().addView(label);
    }

    protected void addTextEditor(int id, String value) {
        EditText editText = new EditText(this);
        editText.setText(value);
        editText.setSingleLine(true);
        editText.setId(id);
        getContainer().addView(editText);
    }

    protected void addDateEditor(int id, Date value) {
        EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
        Format dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        String hint = ((SimpleDateFormat) dateFormat).toLocalizedPattern();
        editText.setHint(hint);
        editText.setText(((SimpleDateFormat) dateFormat).format(value));
        editText.setId(id);
        getContainer().addView(editText);
    }

    protected void addSpinner(int id, List<String> items, String value) {
        Spinner spinner = new Spinner(this);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        int position = items.indexOf(value);
        if (position > -1) {
            spinner.setSelection(position);
        }
        spinner.setId(id);
        getContainer().addView(spinner);
    }

    protected void addEconomicActivityPicker(final int id, final int economicActivityId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        final Button button = (Button) inflater.inflate(R.layout.spinner_button, getContainer(), false);
        button.setText("");
        button.setId(id);
        button.setTag(economicActivityId);
        getContainer().addView(button);

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

    protected void addBranchPicker(final int id, final int branchId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        final Button button = (Button) inflater.inflate(R.layout.spinner_button, getContainer(), false);
        button.setText("");
        button.setId(id);
        button.setTag(branchId);
        getContainer().addView(button);

        final Context context = this;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BranchPickerActivity.class);
                intent.putExtra("branchPickerId", id);
                intent.putExtra("branchId", (Integer) button.getTag());
                startActivityForResult(intent, PICK_BRANCH_REQUEST);
            }
        });

        LoadBranchEvent event = new LoadBranchEvent();
        event.actionId = id;
        event.branchId = branchId;
        bus.post(event);
    }

    public void onEvent(BranchLoadedEvent event) {
        ViewGroup viewGroup = getContainer();
        Button button = (Button) viewGroup.findViewById(event.actionId);
        if (button != null && event.branch != null) {
            button.setText(event.branch.name);
            button.setTag(event.branch.id);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_ECONOMIC_ACTIVITY_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                LoadEconomicActivityEvent event = new LoadEconomicActivityEvent();
                event.actionId = data.getIntExtra("economicActivityPickerId", 0);
                event.economicActivityId = data.getIntExtra("economicActivityId", 0);
                enqueueEvent(event);
            }
        } else if (requestCode == PICK_BRANCH_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                LoadBranchEvent event = new LoadBranchEvent();
                event.actionId = data.getIntExtra("branchPickerId", 0);
                event.branchId = data.getIntExtra("branchId", 0);
                enqueueEvent(event);
            }
        }
    }
}
