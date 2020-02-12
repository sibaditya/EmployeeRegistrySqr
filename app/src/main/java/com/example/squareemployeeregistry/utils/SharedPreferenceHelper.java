package com.example.squareemployeeregistry.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.squareemployeeregistry.Application.AppController;

import java.util.Set;

public class SharedPreferenceHelper {

  private static final String EMPLOYEE_LIST_SUCCESS_RESPONSE = "EMPLOYEE_LIST_SUCCESS_RESPONSE";

  // Keep static copy of the this Application shared preferences
  private static SharedPreferences mSquarePreferences;

  /**
   * Return shared preferences specific to this application. Can be null.
   */
  public static SharedPreferences getSharedPreference() {
    synchronized (SharedPreferenceHelper.class) {
      if (mSquarePreferences == null) {
        Context context = AppController.getInstance().getApplicationContext();
        if (context == null)
          return null;
        mSquarePreferences = context.getSharedPreferences(
                "com.example.squareemployeeregistry", Context.MODE_PRIVATE);
      }
    }
    return mSquarePreferences;
  }

  public static boolean putBoolean(String key, boolean value) {
    SharedPreferences prefs = getSharedPreference();
    return prefs != null && prefs.edit().putBoolean(key, value).commit();
  }

  public static boolean putFloat(String key, float value) {
    SharedPreferences prefs = getSharedPreference();
    return prefs != null && prefs.edit().putFloat(key, value).commit();
  }

  public static boolean putInt(String key, int value) {
    SharedPreferences prefs = getSharedPreference();
    return prefs != null && prefs.edit().putInt(key, value).commit();
  }

  public static boolean putLong(String key, long value) {
    SharedPreferences prefs = getSharedPreference();
    return prefs != null && prefs.edit().putLong(key, value).commit();
  }

  public static boolean putString(String key, String value) {
    SharedPreferences prefs = getSharedPreference();
    return prefs != null && prefs.edit().putString(key, value).commit();
  }

  public static boolean putStringSet(String key, Set<String> value) {
    SharedPreferences prefs = getSharedPreference();
    return prefs != null && prefs.edit().putStringSet(key, value).commit();
  }

  public static boolean getBoolean(String key, boolean defValue) {
    SharedPreferences prefs = getSharedPreference();
    if (prefs == null)
      return defValue;

    return prefs.getBoolean(key, defValue);
  }

  public static float getFloat(String key, float defValue) {
    SharedPreferences prefs = getSharedPreference();
    if (prefs == null)
      return defValue;

    return prefs.getFloat(key, defValue);
  }

  public static int getInt(String key, int defValue) {
    SharedPreferences prefs = getSharedPreference();
    if (prefs == null)
      return defValue;

    return prefs.getInt(key, defValue);
  }

  public static long getLong(String key, long defValue) {
    SharedPreferences prefs = getSharedPreference();
    if (prefs == null)
      return defValue;

    return prefs.getLong(key, defValue);
  }

  public static String getString(String key, String defValue) {
    SharedPreferences prefs = getSharedPreference();
    if (prefs == null)
      return defValue;

    return prefs.getString(key, defValue);
  }

  public static Set<String> getStringSet(String key, Set<String> defValue) {
    SharedPreferences prefs = getSharedPreference();
    if (prefs == null)
      return defValue;

    return prefs.getStringSet(key, defValue);
  }


  public static void clearAllPreferences() {
    SharedPreferences prefs = getSharedPreference();
    if (prefs != null) {
      prefs.edit().clear().apply();
    }
  }
}