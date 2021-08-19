package com.acube.jims.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FilterPreference {
    private static final boolean DEFAULT_BOOLEAN = false;
    private static final String DEFAULT_STRING = "";
    private static final Integer DEFAULT_INTEGER = 0;
    private static final String FilterPref = "Filter";
    private static SharedPreferences sharedpreferences;


    //store string preference
    public static void storeStringPreference(Context context, String key, String value) {
        sharedpreferences = context.getSharedPreferences(FilterPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    //store int preference
    public static void storeIntegerPreference(Context context, String key, Integer value) {
        sharedpreferences = context.getSharedPreferences(FilterPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    //return string preferences
    public static String retrieveStringPreferences(Context context, String key) {
        sharedpreferences = context.getSharedPreferences(FilterPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        return sharedpreferences.getString(key, DEFAULT_STRING);
    }

    //store boolean preference
    public static void storeBooleanPreference(Context context, String key, boolean value) {
        sharedpreferences = context.getSharedPreferences(FilterPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    //return boolean preference
    public static boolean retrieveBooleanPreferences(Context context, String key) {
        sharedpreferences = context.getSharedPreferences(FilterPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        return sharedpreferences.getBoolean(key, DEFAULT_BOOLEAN);
    }

    //return integrer preference
    public static Integer retrieveIntegerPreferences(Context context, String key) {
        sharedpreferences = context.getSharedPreferences(FilterPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        return sharedpreferences.getInt(key, DEFAULT_INTEGER);

    }

    //Removing all local preferences
    public static void clearPreferences(Context context) {
        sharedpreferences = context.getSharedPreferences(FilterPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.apply();
    }

    //Removing all local preferences
    public static void removePreferences(Context context, String key) {
        sharedpreferences = context.getSharedPreferences(FilterPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
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
