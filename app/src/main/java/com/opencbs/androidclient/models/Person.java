package com.opencbs.androidclient.models;

import java.util.Date;
import java.util.List;

public class Person {
    public int id;
    public String uuid;
    public String firstName;
    public String lastName;
    public String fatherName;
    public String sex;
    public Date birthDate;
    public String birthPlace;
    public String identificationData;
    public String nationality;
    public int activityId;
    public int branchId;
    public String personalPhone;
    public String homePhone;
    public String email;
    public Address address1;
    public Address address2;
    public List<CustomValue> customInformation;

    public String getCustomFieldValue(int fieldId) {
        for (CustomValue value : customInformation) {
            if (value.field.id == fieldId) {
                return value.value;
            }
        }
        return "";
    }
}
