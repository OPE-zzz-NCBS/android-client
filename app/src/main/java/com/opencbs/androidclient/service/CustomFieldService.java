package com.opencbs.androidclient.service;

import com.opencbs.androidclient.event.PersonCustomFieldsLoadedEvent;
import com.opencbs.androidclient.event.LoadPersonCustomFieldsEvent;
import com.opencbs.androidclient.repo.CustomFieldRepo;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class CustomFieldService {

    @Inject
    EventBus bus;

    @Inject
    CustomFieldRepo customFieldRepo;

    public void onEvent(LoadPersonCustomFieldsEvent event) {
        PersonCustomFieldsLoadedEvent responseEvent = new PersonCustomFieldsLoadedEvent();
        responseEvent.customFields = customFieldRepo.getPersonCustomFields();
        bus.post(responseEvent);
    }
}
