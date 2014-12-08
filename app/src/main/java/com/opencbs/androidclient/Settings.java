package com.opencbs.androidclient;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {

    private final static String NAME = "opencbs";

    public static final int NOT_CACHED = 0;
    public static final int CACHING = 1;
    public static final int CACHED = 2;

    private Context context;

    public Settings(Context context) {
        this.context = context;
    }

    private String getStringValue(String name) {
        return context.getSharedPreferences(NAME, Context.MODE_PRIVATE).getString(name, "");
    }

    private void putStringValue(String name, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit();
        editor.putString(name, value);
        editor.commit();
    }

    private int getIntValue(String name) {
        return context.getSharedPreferences(NAME, Context.MODE_PRIVATE).getInt(name, 0);
    }

    private void putIntValue(String name, int value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(name, value);
        editor.commit();
    }

    public String getEndpoint() {
        return getStringValue("endpoint");
    }

    public void setEndpoint(String endpoint) {
        putStringValue("endpoint", endpoint);
    }

    public String getAccessToken() {
        return getStringValue("access_token");
    }

    public void setAccessToken(String accessToken) {
        putStringValue("access_token", accessToken);
    }

    public int getDataState() {
        return getIntValue("data_state");
    }

    public void setDataState(int dataState) {
        putIntValue("data_state", dataState);
    }
}
