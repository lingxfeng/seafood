package com.eastinno.otransos.mfang_base;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreference 存储
 * 
 * @author wxc
 */
public class SharedPreferencesUtil {
    private static final String SPNAME_STRING = "mfang";

    public static void saveStr(String keyName, String content, Context context) {
        SharedPreferences mSharedPreference = context.getSharedPreferences(SPNAME_STRING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putString(keyName, content);
        editor.commit();
    }

    public static String getStrByName(String keyName, Context context) {
        SharedPreferences mSharedPreference = context.getSharedPreferences(SPNAME_STRING, Context.MODE_PRIVATE);
        return mSharedPreference.getString(keyName, "");
    }

    public static String getStrByName(String keyName, Context context, String defaultString) {
        SharedPreferences mSharedPreference = context.getSharedPreferences(SPNAME_STRING, Context.MODE_PRIVATE);
        return mSharedPreference.getString(keyName, defaultString);
    }

    public static void saveInt(String keyName, int value, Context context) {

        SharedPreferences mSharedPreference = context.getSharedPreferences(SPNAME_STRING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putInt(keyName, value);
        editor.commit();
    }

    public static int getIntByName(String keyName, Context context) {
        SharedPreferences mSharedPreference = context.getSharedPreferences(SPNAME_STRING, Context.MODE_PRIVATE);
        return mSharedPreference.getInt(keyName, 0);
    }

    public static int getIntByName(String keyName, int defaultValue, Context context) {
        SharedPreferences mSharedPreference = context.getSharedPreferences(SPNAME_STRING, Context.MODE_PRIVATE);
        return mSharedPreference.getInt(keyName, defaultValue);
    }

    public static void saveBool(String keyName, boolean value, Context context) {
        SharedPreferences mSharedPreference = context.getSharedPreferences(SPNAME_STRING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putBoolean(keyName, value);
        editor.commit();

    }

    public static boolean getBoolByName(String keyName, Context context) {
        SharedPreferences mSharedPreference = context.getSharedPreferences(SPNAME_STRING, Context.MODE_PRIVATE);
        return mSharedPreference.getBoolean(keyName, false);
    }

    public static boolean getBoolByName(String keyName, boolean defaltValue, Context context) {
        SharedPreferences mSharedPreference = context.getSharedPreferences(SPNAME_STRING, Context.MODE_PRIVATE);
        return mSharedPreference.getBoolean(keyName, defaltValue);
    }

    public static void saveLong(String keyName, long value, Context context) {
        SharedPreferences mSharedPreference = context.getSharedPreferences(SPNAME_STRING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putLong(keyName, value);
        editor.commit();
    }

    public static long getLongByName(String keyName, Context context) {
        SharedPreferences mSharedPreference = context.getSharedPreferences(SPNAME_STRING, Context.MODE_PRIVATE);
        return mSharedPreference.getLong(keyName, -1);
    }

    public static long getLongByName(String keyName, long defaultValue, Context context) {
        SharedPreferences mSharedPreference = context.getSharedPreferences(SPNAME_STRING, Context.MODE_PRIVATE);
        return mSharedPreference.getLong(keyName, defaultValue);
    }
}
