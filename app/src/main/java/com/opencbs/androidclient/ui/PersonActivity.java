package com.opencbs.androidclient.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.opencbs.androidclient.R;
import com.opencbs.androidclient.event.PersonCustomFieldsLoadedEvent;
import com.opencbs.androidclient.event.LoadPersonCustomFieldsEvent;
import com.opencbs.androidclient.event.LoadPersonEvent;
import com.opencbs.androidclient.event.PersonLoadedEvent;
import com.opencbs.androidclient.model.Address;
import com.opencbs.androidclient.model.CustomField;
import com.opencbs.androidclient.model.CustomValue;
import com.opencbs.androidclient.model.Person;
import com.opencbs.androidclient.ui.validation.DateValidationRule;
import com.opencbs.androidclient.ui.validation.RequiredValidationRule;
import com.opencbs.androidclient.ui.validation.Validator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PersonActivity extends EditorActivity {

    private static final int FIRST_NAME_VIEW_ID = 1;
    private static final int LAST_NAME_VIEW_ID = 2;
    private static final int FATHER_NAME_VIEW_ID = 3;
    private static final int BIRTH_DATE_VIEW_ID = 4;
    private static final int BIRTH_PLACE_VIEW_ID = 5;
    private static final int SEX_VIEW_ID = 6;
    private static final int IDENTIFICATION_DATA_VIEW_ID = 7;
    private static final int NATIONALITY_VIEW_ID = 8;
    private static final int ECONOMIC_ACTIVITY_VIEW_ID = 9;
    private static final int BRANCH_VIEW_ID = 10;
    private static final int HOME_PHONE_VIEW_ID = 11;
    private static final int PERSONAL_PHONE_VIEW_ID = 12;
    private static final int EMAIL_VIEW_ID = 13;

    private static final int REGION_1_VIEW_ID = 14;
    private static final int DISTRICT_1_VIEW_ID = 15;
    private static final int CITY_1_VIEW_ID = 16;
    private static final int ADDRESS_1_VIEW_ID = 17;
    private static final int POSTAL_CODE_1_VIEW_ID = 18;

    private static final int REGION_2_VIEW_ID = 19;
    private static final int DISTRICT_2_VIEW_ID = 20;
    private static final int CITY_2_VIEW_ID = 21;
    private static final int ADDRESS_2_VIEW_ID = 22;
    private static final int POSTAL_CODE_2_VIEW_ID = 23;

    private static final int CUSTOM_VIEW_BASE_ID = 1000;

    private ViewGroup container;
    private List<CustomField> customFields;
    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        validator = new Validator();

        LoadPersonCustomFieldsEvent event = new LoadPersonCustomFieldsEvent();
        bus.post(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_person, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                save();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected ViewGroup getContainer() {
        if (container != null) return container;
        container = (ViewGroup) findViewById(R.id.person_layout);
        return container;
    }

    public void onEvent(PersonCustomFieldsLoadedEvent event) {
        customFields = event.customFields;

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        if (id > 0) {
            LoadPersonEvent loadPersonEvent = new LoadPersonEvent(id);
            bus.post(loadPersonEvent);
        } else {
            Person person = new Person();
            person.address1 = new Address();
            person.address2 = new Address();
            person.customInformation = new ArrayList<CustomValue>();
            bus.post(new PersonLoadedEvent(person));
        }
    }

    public void onEvent(PersonLoadedEvent event) {
        setPerson(event.person);
        setupValidator();
    }

    private void setPerson(Person person) {
        ViewGroup layout = (ViewGroup) findViewById(R.id.person_layout);
        if (layout.getChildCount() > 0) {
            layout.removeAllViews();
        }

        addLabel("First name", true);
        addTextEditor(FIRST_NAME_VIEW_ID, person.firstName);

        addLabel("Father name");
        addTextEditor(FATHER_NAME_VIEW_ID, person.fatherName);

        addLabel("Last name", true);
        addTextEditor(LAST_NAME_VIEW_ID, person.lastName);

        addLabel("Birth date", true);
        addDateEditor(BIRTH_DATE_VIEW_ID, person.birthDate);

        addLabel("Birth place");
        addTextEditor(BIRTH_PLACE_VIEW_ID, person.birthPlace);

        addLabel("Sex", true);
        ArrayList<String> list = new ArrayList<String>();
        list.add("Male");
        list.add("Female");
        addSpinner(SEX_VIEW_ID, list, person.sex);

        addLabel("Identification data", true);
        addTextEditor(IDENTIFICATION_DATA_VIEW_ID, person.identificationData);

        addLabel("Nationality");
        addTextEditor(NATIONALITY_VIEW_ID, person.nationality);

        addLabel("Economic activity", true);
        addEconomicActivityPicker(ECONOMIC_ACTIVITY_VIEW_ID, person.activityId);

        addLabel("Branch", true);
        addBranchPicker(BRANCH_VIEW_ID, person.branchId);

        addLabel("Home phone");
        addTextEditor(HOME_PHONE_VIEW_ID, person.homePhone);

        addLabel("Personal phone");
        addTextEditor(PERSONAL_PHONE_VIEW_ID, person.personalPhone);

        addLabel("Email");
        addTextEditor(EMAIL_VIEW_ID, person.email);

        addSection("ADDRESS 1");

        addLabel("Region", true);
        addTextEditor(REGION_1_VIEW_ID, "").setEnabled(false);

        addLabel("District", true);
        addTextEditor(DISTRICT_1_VIEW_ID, "").setEnabled(false);

        addLabel("City", true);
        addCityPicker(CITY_1_VIEW_ID, person.address1.cityId);

        addLabel("Address");
        addTextEditor(ADDRESS_1_VIEW_ID, person.address1.address).setSingleLine(false);

        addLabel("Postal code");
        addTextEditor(POSTAL_CODE_1_VIEW_ID, person.address1.postalCode);

        addSection("ADDRESS 2");

        addLabel("Region");
        addTextEditor(REGION_2_VIEW_ID, "").setEnabled(false);

        addLabel("District");
        addTextEditor(DISTRICT_2_VIEW_ID, "").setEnabled(false);

        addLabel("City");
        addCityPicker(CITY_2_VIEW_ID, person.address2.cityId);

        addLabel("Address");
        addTextEditor(ADDRESS_2_VIEW_ID, person.address2.address).setSingleLine(false);

        addLabel("Postal code");
        addTextEditor(POSTAL_CODE_2_VIEW_ID, person.address2.postalCode);

        String currentCustomSection = "";
        for (CustomField field : customFields) {
            if (!currentCustomSection.equals(field.tab)) {
                addSection(field.tab.toUpperCase());
                currentCustomSection = field.tab;
            }
            addCustomValue(field, CUSTOM_VIEW_BASE_ID + field.id, person.getCustomFieldValue(field.id));
        }
    }

    private void setupValidator() {
        ViewGroup container = getContainer();

        String errorRequiredField = getString(R.string.error_required_field);
        String errorInvalidEntry = getString(R.string.error_invalid_entry);
        SimpleDateFormat dateFormat = (SimpleDateFormat) android.text.format.DateFormat.getDateFormat(getApplicationContext());

        validator.addRule(new RequiredValidationRule(container, FIRST_NAME_VIEW_ID, errorRequiredField));
        validator.addRule(new RequiredValidationRule(container, LAST_NAME_VIEW_ID, errorRequiredField));
        validator.addRule(new RequiredValidationRule(container, BIRTH_DATE_VIEW_ID, errorRequiredField));
        validator.addRule(new DateValidationRule(container, BIRTH_DATE_VIEW_ID, dateFormat, errorInvalidEntry));
        validator.addRule(new RequiredValidationRule(container, IDENTIFICATION_DATA_VIEW_ID, errorRequiredField));
        validator.addRule(new RequiredValidationRule(container, CITY_1_VIEW_ID, errorRequiredField));

        for (CustomField field : customFields) {
            int fieldId = CUSTOM_VIEW_BASE_ID + field.id;
            if (field.type.equals("Text")) {
                if (field.mandatory) {
                    validator.addRule(new RequiredValidationRule(container, fieldId, errorRequiredField));
                }
            } else if (field.type.equals("Number")) {
                if (field.mandatory) {
                    validator.addRule(new RequiredValidationRule(container, fieldId, errorRequiredField));
                }
            } else if (field.type.equals("Date")) {
                if (field.mandatory) {
                    validator.addRule(new RequiredValidationRule(container, fieldId, errorRequiredField));
                }
                validator.addRule(new DateValidationRule(container, fieldId, dateFormat, errorInvalidEntry));
            }
        }
    }

    private Person getPerson() {
        Person person = new Person();
        person.firstName = getTextValue(FIRST_NAME_VIEW_ID);
        person.fatherName = getTextValue(FATHER_NAME_VIEW_ID);
        person.lastName = getTextValue(LAST_NAME_VIEW_ID);
        person.birthDate = getDateValue(BIRTH_DATE_VIEW_ID);
        person.birthPlace = getTextValue(BIRTH_PLACE_VIEW_ID);
        person.sex = getSpinnerValue(SEX_VIEW_ID);
        person.identificationData = getTextValue(IDENTIFICATION_DATA_VIEW_ID);
        person.nationality = getTextValue(NATIONALITY_VIEW_ID);
        person.activityId = getEconomicActivityId(ECONOMIC_ACTIVITY_VIEW_ID);
        person.branchId = getBranchId(BRANCH_VIEW_ID);
        person.homePhone = getTextValue(HOME_PHONE_VIEW_ID);
        person.personalPhone = getTextValue(PERSONAL_PHONE_VIEW_ID);
        person.email = getTextValue(EMAIL_VIEW_ID);
        person.address1 = new Address();
        person.address1.cityId = getCityId(CITY_1_VIEW_ID);
        person.address1.address = getTextValue(ADDRESS_1_VIEW_ID);
        person.address1.postalCode = getTextValue(POSTAL_CODE_1_VIEW_ID);
        person.address2 = new Address();
        person.address2.cityId = getCityId(CITY_2_VIEW_ID);
        person.address2.address = getTextValue(ADDRESS_2_VIEW_ID);
        person.address2.postalCode = getTextValue(POSTAL_CODE_2_VIEW_ID);
        return person;
    }

    private void save() {
        if (!validator.validate()) return;

        //Person person = getPerson();
    }
}
