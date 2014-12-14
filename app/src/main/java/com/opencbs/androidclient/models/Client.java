package com.opencbs.androidclient.models;

public class Client {
    public int id;
    public String uuid;
    public String name;
    public String type;

    public boolean isSynced() {
        return id > 0;
    }
}
