package com.opencbs.androidclient.validators;

import android.view.ViewGroup;

public class RequiredValidationRule extends ValidationRule {

    public RequiredValidationRule(ViewGroup container, int viewId, String errorMessage) {
        super(container, viewId, errorMessage);
    }

    @Override
    protected boolean executeImpl(String value) {
        return value.length() > 0;
    }
}
