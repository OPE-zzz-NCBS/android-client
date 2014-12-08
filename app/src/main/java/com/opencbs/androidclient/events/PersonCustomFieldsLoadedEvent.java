package com.opencbs.androidclient.events;

import com.opencbs.androidclient.models.CustomField;

import java.util.List;

public class PersonCustomFieldsLoadedEvent extends BusEvent {
    public List<CustomField> customFields;
}
