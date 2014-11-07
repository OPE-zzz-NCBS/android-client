package com.opencbs.androidclient;

import android.content.Context;
import android.content.SharedPreferences;

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

    public static String getAccessToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("opencbs", Context.MODE_PRIVATE);
        return preferences.getString("access_token", "");
    }

    public static void setAccessToken(Context context, String accessToken) {
        SharedPreferences preferences = context.getSharedPreferences("opencbs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("access_token", accessToken);
        editor.commit();
    }
}
