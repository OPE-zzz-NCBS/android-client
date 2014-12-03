package com.opencbs.androidclient.ui.validation;

import java.util.ArrayList;
import java.util.List;

public class Validator {
    private List<ValidationRule> rules;

    public Validator() {
        rules = new ArrayList<ValidationRule>();
    }

    public void addRule(ValidationRule rule) {
        rules.add(rule);
    }

    public boolean validate() {
        boolean result = true;
        for (ValidationRule rule : rules) {
            boolean valid = rule.execute();
            result = result && valid;
        }
        return result;
    }
}
