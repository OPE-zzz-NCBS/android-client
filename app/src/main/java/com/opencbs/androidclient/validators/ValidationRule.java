package com.opencbs.androidclient.validators;

import android.view.ViewGroup;
import android.widget.TextView;

public abstract class ValidationRule {
    protected ViewGroup container;
    protected int viewId;
    protected String errorMessage;

    public ValidationRule(ViewGroup container, int viewId, String errorMessage) {
        this.container = container;
        this.viewId = viewId;
        this.errorMessage = errorMessage;
    }

    public boolean   execute() {
        TextView textView = (TextView) container.findViewById(viewId);
        boolean valid = executeImpl(textView.getText().toString());
        if (!valid) {
            textView.setError(errorMessage);
        }
        return valid;
    }

    protected abstract boolean executeImpl(String value);
}
