package com.example.squareemployeeregistry.repository;

import android.os.Looper;

import com.example.squareemployeeregistry.BuildConfig;

public class Asserts {

  public static boolean sRunningAutomation = false;

  public static void on(boolean condition, String message){
    if (!sRunningAutomation){
      if (!condition)
        throw new RuntimeException("Assert: " + message);
    }
  }

  public static void throwException(String message) {
    on(false, message);
  }

  public static void isOnMainThread(){
    on(Looper.myLooper() == Looper.getMainLooper(), "not on main thread");
  }

  public static void isNotOnMainThread(){
    on(Looper.myLooper() != Looper.getMainLooper(), "is on main thread");
  }

}