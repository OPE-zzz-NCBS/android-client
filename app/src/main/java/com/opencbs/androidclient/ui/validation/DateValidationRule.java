package com.opencbs.androidclient.ui.validation;

import android.view.ViewGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateValidationRule extends ValidationRule {

    private SimpleDateFormat dateFormat;

    public DateValidationRule(ViewGroup container, int viewId, SimpleDateFormat dateFormat, String errorMessage) {
        super(container, viewId, errorMessage);
        this.dateFormat = dateFormat;
    }

    @Override
    protected boolean executeImpl(String value) {
        if (value.length() == 0) return true;
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat.toLocalizedPattern());
        sdf.setLenient(false);
        try {
            sdf.parse(value);
            return true;
        } catch (ParseException ignored) {
            return false;
        }
    }
}
