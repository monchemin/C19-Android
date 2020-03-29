package com.digitalink.c19.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.digitalink.c19.R;

public class Preference {
    private static String ID = "ID";
    private static String PHONE_NUMBER = "PHONE_NUMBER";

    public static void setActive(Context context, String id, String pn) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.key_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(ID, id);
        editor.putString(PHONE_NUMBER, pn);
        editor.apply();

    }

    public static String GetID(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.key_file), Context.MODE_PRIVATE);
        return sharedPref.getString(ID, "");
    }

    public static String GetPhoneNumber(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.key_file), Context.MODE_PRIVATE);
        return sharedPref.getString(PHONE_NUMBER, "");
    }

    public static void Disconnect(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.key_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }
}
