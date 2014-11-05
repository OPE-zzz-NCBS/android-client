package com.opencbs.androidclient;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Pavel on 11/5/2014.
 */
public class Settings {
    public static String getEndpoint(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("opencbs", Context.MODE_PRIVATE);
        return preferences.getString("endpoint", "");
    }

    public static void setEndpoint(Context context, String endpoint) {
        SharedPreferences preferences = context.getSharedPreferences("opencbs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("endpoint", endpoint);
        editor.commit();
    }
}
