package com.opencbs.androidclient.event;

import com.opencbs.androidclient.model.CustomField;

import java.util.List;

public class PersonCustomFieldsLoadedEvent extends BusEvent {
    public List<CustomField> customFields;
}
