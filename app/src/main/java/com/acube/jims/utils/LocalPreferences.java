package com.acube.jims.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.acube.jims.datalayer.constants.AppConstants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class LocalPreferences {
    private static final boolean DEFAULT_BOOLEAN = false;
    private static final String DEFAULT_STRING = "";
    private static final Integer DEFAULT_INTEGER = -1;
    private static final float DEFAULT_READFLOATVALUE = 30.0f;

    public static void storeReadfloatPreference(Context context, String key, float value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    //store string preference
    public static void storeStringPreference(Context context, String key, String value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(key, value);
        editor.apply();
    }
    public static void setbaseUrl(Context context, String value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString("BaseUrl", value);
        editor.apply();
    }
    public static String getBaseUrl(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString("BaseUrl", DEFAULT_STRING);
    }
    public static void storeAuthenticationToken(Context context, String value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString("Token", value);
        editor.apply();
    }

    public static String getToken(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return AppConstants.Authorization+prefs.getString("Token", DEFAULT_STRING);
    }
    public static Float retrieveReadFloatPreferences(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getFloat(key, DEFAULT_READFLOATVALUE);

    }
    //store int preference
    public static void storeIntegerPreference(Context context, String key, Integer value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    //return string preferences
    public static String retrieveStringPreferences(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(key, DEFAULT_STRING);
    }

    //store boolean preference
    public static void storeBooleanPreference(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    //return boolean preference
    public static boolean retrieveBooleanPreferences(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(key, DEFAULT_BOOLEAN);
    }

    //return integrer preference
    public static Integer retrieveIntegerPreferences(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(key, DEFAULT_INTEGER);

    }

    //Removing all local preferences
    public static void clearPreferences(Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.clear();
        editor.apply();
    }

    //Removing all local preferences
    public static void removePreferences(Context context, String key) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.remove(key);
        editor.apply();
    }

    //Storing arraylist<string> to the local preference
    public static void storeSetPreference(Context context, String key, ArrayList<String> value) {

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        Set<String> set = new HashSet<String>();
        set.addAll(value);
        editor.putStringSet(key, set);
        editor.apply();
    }

    //for return arraylist<String> from local preference
    public static ArrayList<String> retrieveSetPreferences(Context context, String key) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> set = new HashSet<String>();
        Set<String> setvalue = prefs.getStringSet(key, set);

        return new ArrayList<String>(setvalue);
    }
}
