package com.opencbs.androidclient.model;

public class Branch {
    public int id;
    public String name;
    public String code;
    public String description;
    public String address;

    @Override
    public String toString() {
        return name;
    }
}
