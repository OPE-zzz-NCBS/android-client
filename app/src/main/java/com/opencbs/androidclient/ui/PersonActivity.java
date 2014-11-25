package com.opencbs.androidclient.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

import com.opencbs.androidclient.R;
import com.opencbs.androidclient.event.LoadPersonEvent;
import com.opencbs.androidclient.event.PersonLoadedEvent;
import com.opencbs.androidclient.model.Person;

import java.util.ArrayList;

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
    private static final int CITY_VIEW_ID = 11;

    private ViewGroup container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        Intent intent = getIntent();

        LoadPersonEvent event = new LoadPersonEvent();
        event.id = intent.getIntExtra("id", 0);
        enqueueEvent(event);
    }

    @Override
    protected ViewGroup getContainer() {
        if (container != null) return container;
        container = (ViewGroup) findViewById(R.id.person_layout);
        return container;
    }

    public void onEvent(PersonLoadedEvent event) {
        setPerson(event.person);
    }

    private void setPerson(Person person) {
        ViewGroup layout = (ViewGroup) findViewById(R.id.person_layout);
        if (layout.getChildCount() > 0) {
            layout.removeAllViews();
        }

        addLabel("First name");
        addTextEditor(FIRST_NAME_VIEW_ID, person.firstName);

        addLabel("Father name");
        addTextEditor(FATHER_NAME_VIEW_ID, person.fatherName);

        addLabel("Last name");
        addTextEditor(LAST_NAME_VIEW_ID, person.lastName);

        addLabel("Birth date");
        addDateEditor(BIRTH_DATE_VIEW_ID, person.birthDate);

        addLabel("Birth place");
        addTextEditor(BIRTH_PLACE_VIEW_ID, person.birthPlace);

        addLabel("Sex");
        ArrayList<String> list = new ArrayList<String>();
        list.add("Male");
        list.add("Female");
        addSpinner(SEX_VIEW_ID, list, person.sex);

        addLabel("Identification data");
        addTextEditor(IDENTIFICATION_DATA_VIEW_ID, person.identificationData);

        addLabel("Nationality");
        addTextEditor(NATIONALITY_VIEW_ID, person.nationality);

        addLabel("Economic activity");
        addEconomicActivityPicker(ECONOMIC_ACTIVITY_VIEW_ID, person.activityId);

        addLabel("Branch");
        addBranchPicker(BRANCH_VIEW_ID, person.branchId);

        addLabel("City");
        addCityPicker(CITY_VIEW_ID, "");
    }
}
