package com.opencbs.androidclient.model;

public class District {
    public int id;
    public String name;
    public int regionId;

    @Override
    public String toString() {
        return name;
    }
}
