package com.opencbs.androidclient.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.opencbs.androidclient.Field;
import com.opencbs.androidclient.R;
import com.opencbs.androidclient.event.LoadPersonEvent;
import com.opencbs.androidclient.event.PersonLoadedEvent;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class PersonActivity extends BaseActivity {

    @Inject
    EventBus bus;

    private int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

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
        loadPerson();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    private void loadPerson() {
        LoadPersonEvent event = new LoadPersonEvent();
        event.id = id;
        bus.post(event);
    }

    public void onEvent(PersonLoadedEvent event) {
        LinearLayout ll = (LinearLayout) findViewById(R.id.person_layout);
        if (ll.getChildCount() > 0) {
            ll.removeAllViews();
        }

        for (Field field : event.person.fields) {
            TextView label = new TextView(this);
            label.setText(field.caption);
            label.setPadding(0, 48, 0, 0);
            label.setTextColor(Color.parseColor("#666666"));
            ll.addView(label);

            if (field.dataType.equals("TEXT")) {
                EditText editText = new EditText(this);
                editText.setText(field.value);
                editText.setSingleLine(true);
                ll.addView(editText);
            } else if (field.dataType.equals("DATE")) {
                EditText editText = new EditText(this);
                editText.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
                Format dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
                String hint = ((SimpleDateFormat) dateFormat).toLocalizedPattern();
                editText.setHint(hint);

                String formatString = "yyyy-MM-dd'T'HH:mm:ss";
                SimpleDateFormat inFormat = new SimpleDateFormat(formatString);
                try {
                    Date date = inFormat.parse(field.value);
                    editText.setText(((SimpleDateFormat) dateFormat).format(date));
                } catch (ParseException e) {
                }
                ll.addView(editText);
            } else if (field.dataType.equals("LIST")) {
                Spinner spinner = new Spinner(this);
                ArrayList<String> array = (ArrayList<String>) field.extra;
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, array);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerAdapter);
                int position = array.indexOf(field.value);
                if (position > -1) {
                    spinner.setSelection(position);
                }
                ll.addView(spinner);
            }
        }
    }
}
