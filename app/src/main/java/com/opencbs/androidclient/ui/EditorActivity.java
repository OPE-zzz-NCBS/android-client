package com.opencbs.androidclient.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.opencbs.androidclient.R;
import com.opencbs.androidclient.event.BranchLoadedEvent;
import com.opencbs.androidclient.event.CitiesLoadedEvent;
import com.opencbs.androidclient.event.CityLoadedEvent;
import com.opencbs.androidclient.event.DistrictLoadedEvent;
import com.opencbs.androidclient.event.EconomicActivityLoadedEvent;
import com.opencbs.androidclient.event.LoadBranchEvent;
import com.opencbs.androidclient.event.LoadCitiesEvent;
import com.opencbs.androidclient.event.LoadCityEvent;
import com.opencbs.androidclient.event.LoadDistrictEvent;
import com.opencbs.androidclient.event.LoadEconomicActivityEvent;
import com.opencbs.androidclient.event.LoadRegionEvent;
import com.opencbs.androidclient.event.RegionLoadedEvent;
import com.opencbs.androidclient.model.City;
import com.opencbs.androidclient.model.CustomField;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public abstract class EditorActivity extends ActivityWithBus {

    private static final int PICK_ECONOMIC_ACTIVITY_REQUEST = 1;
    private static final int PICK_BRANCH_REQUEST = 2;

    private static final int CUSTOM_VIEW_BASE_ID = 1000;

    public void onEvent(EconomicActivityLoadedEvent event) {
        ViewGroup viewGroup = getContainer();
        Button button = (Button) viewGroup.findViewById(event.actionId);
        if (button != null && event.economicActivity != null) {
            button.setText(event.economicActivity.name);
            button.setTag(event.economicActivity.id);
        }
    }

    protected abstract ViewGroup getContainer();

    protected void addLabel(String text, boolean required) {
        TextView label = new TextView(this);
        if (!required) {
            label.setText(text);
            label.setTextColor(Color.parseColor("#666666"));
        } else {
            SpannableStringBuilder builder = new SpannableStringBuilder();
            builder.append(text).append(" ");
            int start = 0;
            int end = builder.length();
            builder.append("*");
            builder.setSpan(new ForegroundColorSpan(Color.parseColor("#666666")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new ForegroundColorSpan(Color.RED), end, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            label.setText(builder);
        }
        label.setLayoutParams(getLabelLayoutParams());
        getContainer().addView(label);
    }

    protected void addLabel(String text) {
        addLabel(text, false);
    }

    protected void addSection(String text) {
        TextView label = new TextView(this);
        label.setText(text);
        label.setBackgroundColor(Color.parseColor("#cccccc"));
        label.setPadding(24, 24, 24, 24);
        label.setGravity(Gravity.CENTER);
        label.setLayoutParams(getSectionLayoutParams());
        getContainer().addView(label);
    }

    protected EditText addTextEditor(int id, String value) {
        EditText editText = new EditText(this);
        editText.setText(value);
        editText.setSingleLine(true);
        editText.setId(id);
        editText.setLayoutParams(getEditorLayoutParams());
        getContainer().addView(editText);
        return editText;
    }

    protected void addDateEditor(int id, Date value) {
        EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
        Format dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        String hint = ((SimpleDateFormat) dateFormat).toLocalizedPattern();
        editText.setHint(hint);
        if (value != null) {
            editText.setText(((SimpleDateFormat) dateFormat).format(value));
        }
        editText.setId(id);
        editText.setLayoutParams(getEditorLayoutParams());
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
        spinner.setLayoutParams(getEditorLayoutParams());
        getContainer().addView(spinner);
    }

    protected void addEconomicActivityPicker(final int id, final int economicActivityId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        final Button button = (Button) inflater.inflate(R.layout.spinner_button, getContainer(), false);
        button.setText("");
        button.setId(id);
        button.setTag(economicActivityId);
        button.setLayoutParams(getEditorLayoutParams());
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
        button.setLayoutParams(getEditorLayoutParams());
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

    protected void addCityPicker(int id, int cityId) {
        AutoCompleteTextView textView = new AutoCompleteTextView(this);
        textView.setId(id);
        textView.setText("");
        textView.setTag(cityId);
        textView.setLayoutParams(getEditorLayoutParams());
        getContainer().addView(textView);

        LoadCitiesEvent event = new LoadCitiesEvent();
        event.selector = id;
        bus.post(event);

        LoadCityEvent loadCityEvent = new LoadCityEvent();
        loadCityEvent.selector = id;
        loadCityEvent.cityId = cityId;
        bus.post(loadCityEvent);
    }

    protected void addCheckBox(int id, String label, boolean value) {
        CheckBox checkBox = new CheckBox(this);
        checkBox.setText(label);
        checkBox.setChecked(value);
        checkBox.setLayoutParams(getLabelLayoutParams());
        checkBox.setId(id);
        getContainer().addView(checkBox);
    }

    protected void addCustomValue(CustomField field, String value) {
        String fieldType = field.type;
        boolean required = field.mandatory;
        if (fieldType.equals("List")) {
            addLabel(field.caption, required);
            addListCustomValue(field, value);
        } else if (fieldType.equals("Text")) {
            addLabel(field.caption, required);
            addTextCustomValue(field, value);
        } else if (fieldType.equals("Number")) {
            addLabel(field.caption, required);
            addNumberCustomValue(field, value);
        } else if (fieldType.equals("Date")) {
            addLabel(field.caption, required);
            addDateCustomValue(field, value);
        } else if (fieldType.equals("Boolean")) {
            addBooleanCustomValue(field, value);
        }
    }

    protected void addListCustomValue(CustomField field, String value) {
        String extra = "|" + field.extra;
        List<String> items = Arrays.asList(extra.split(Pattern.quote("|")));
        addSpinner(CUSTOM_VIEW_BASE_ID + field.id, items, value);
    }

    protected void addTextCustomValue(CustomField field, String value) {
        addTextEditor(CUSTOM_VIEW_BASE_ID + field.id, value);
    }

    protected void addNumberCustomValue(CustomField field, String value) {
        EditText editText = addTextEditor(CUSTOM_VIEW_BASE_ID + field.id, value);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    protected void addBooleanCustomValue(CustomField field, String value) {
        boolean checked = Boolean.parseBoolean(value);
        addCheckBox(CUSTOM_VIEW_BASE_ID + field.id, field.caption, checked);
    }

    protected void addDateCustomValue(CustomField field, String value) {
        Date date = null;
        if (!value.isEmpty()) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                date = format.parse(value);
            } catch (ParseException ignored) {
            }
        }
        addDateEditor(CUSTOM_VIEW_BASE_ID + field.id, date);
    }

    public void onEvent(BranchLoadedEvent event) {
        ViewGroup viewGroup = getContainer();
        Button button = (Button) viewGroup.findViewById(event.actionId);
        if (button != null && event.branch != null) {
            button.setText(event.branch.name);
            button.setTag(event.branch.id);
        }
    }

    public void onEvent(CitiesLoadedEvent event) {
        final AutoCompleteTextView textView = (AutoCompleteTextView) getContainer().findViewById(event.selector);
        if (textView == null) return;

        final ArrayAdapter<City> adapter = new ArrayAdapter<City>(this, android.R.layout.simple_dropdown_item_1line, event.cities);
        textView.setAdapter(adapter);
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                City city = adapter.getItem(position);
                if (city == null) return;
                view.setTag(city.id);

                LoadDistrictEvent loadDistrictEvent = new LoadDistrictEvent();
                loadDistrictEvent.selector = textView.getId() - 1;
                loadDistrictEvent.districtId = city.districtId;
                bus.post(loadDistrictEvent);
            }
        });
    }

    public void onEvent(CityLoadedEvent event) {
        AutoCompleteTextView textView = (AutoCompleteTextView) getContainer().findViewById(event.selector);
        if (textView == null) return;
        if (event.city == null) {
            textView.setTag(0);
            return;
        }
        textView.setText(event.city.name);

        LoadDistrictEvent loadDistrictEvent = new LoadDistrictEvent();
        loadDistrictEvent.selector = event.selector - 1;
        loadDistrictEvent.districtId = event.city.districtId;
        bus.post(loadDistrictEvent);
    }

    public void onEvent(DistrictLoadedEvent event) {
        EditText editText = (EditText) getContainer().findViewById(event.selector);
        if (editText == null || event.district == null) return;
        editText.setText(event.district.name);

        LoadRegionEvent loadRegionEvent = new LoadRegionEvent();
        loadRegionEvent.regionId = event.district.regionId;
        loadRegionEvent.selector = event.selector - 1;
        bus.post(loadRegionEvent);
    }

    public void onEvent(RegionLoadedEvent event) {
        EditText editText = (EditText) getContainer().findViewById(event.selector);
        if (editText == null || event.region == null) return;
        editText.setText(event.region.name);
    }

    private LinearLayout.LayoutParams getLabelLayoutParams() {
        LinearLayout.LayoutParams result = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        result.setMargins(48, 96, 48, 0);
        return result;
    }

    private LinearLayout.LayoutParams getEditorLayoutParams() {
        LinearLayout.LayoutParams result = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        result.setMargins(48, 0, 48, 0);
        return result;
    }

    private LinearLayout.LayoutParams getSectionLayoutParams() {
        LinearLayout.LayoutParams result = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        result.setMargins(0, 48, 0, 0);
        return result;
    }
}
