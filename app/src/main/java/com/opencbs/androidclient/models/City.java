package com.opencbs.androidclient.models;

public class City {
    public int id;
    public String name;
    public int districtId;

    @Override
    public String toString() {
        return name;
    }
}
