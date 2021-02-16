package com.sysgen.eom.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class PrefUtil {
    public static final String PREF_FILE = "myproject";

    public static SharedPreferences.Editor getPrefEdit(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_FILE, MODE_PRIVATE);
        return pref.edit();
    }

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = getPrefEdit(context);
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences pref = context.getSharedPreferences(PREF_FILE, MODE_PRIVATE) ;
        return pref.getBoolean(key, defaultValue);
    }

    public static void putInt(Context context, String key, int value) {
        SharedPreferences.Editor editor = getPrefEdit(context);
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences pref = context.getSharedPreferences(PREF_FILE, MODE_PRIVATE);
        return pref.getInt(key, defaultValue);
    }

    public static void putFloat(Context context, String key, float value) {
        SharedPreferences.Editor editor = getPrefEdit(context);
        editor.putFloat(key, value);
        editor.commit();
    }

    public static float getFloat(Context context, String key, float defaultValue) {
        SharedPreferences pref = context.getSharedPreferences(PREF_FILE, MODE_PRIVATE);
        float v = pref.getFloat(key, defaultValue);
        return v;
    }

    public static void putString(Context context, String key, String value) {
        SharedPreferences.Editor editor = getPrefEdit(context);
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(Context context, String key, String defaultString) {
        SharedPreferences pref = context.getSharedPreferences(PREF_FILE, MODE_PRIVATE);
        return pref.getString(key, defaultString);
    }

    public static void removeString(Context context, String key) {
        SharedPreferences.Editor editor = getPrefEdit(context);
        editor.remove(key);
        editor.commit();
    }
}
