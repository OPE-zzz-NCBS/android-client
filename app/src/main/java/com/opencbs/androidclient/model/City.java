package com.opencbs.androidclient.model;

public class City {
    public int id;
    public String name;
    public int districtId;

    @Override
    public String toString() {
        return name;
    }
}
