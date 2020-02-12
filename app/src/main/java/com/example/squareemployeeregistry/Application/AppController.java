package com.example.squareemployeeregistry.Application;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AppController extends Application {

  public static final String TAG = AppController.class.getSimpleName();
  private static AppController mInstance;
  private RequestQueue mRequestQueue;


  public static synchronized AppController getInstance() {
    return mInstance;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    Log.d(TAG, "onCreate");
    mInstance = this;
  }

  public RequestQueue getRequestQueue() {
    Log.d(TAG, "getRequestQueue");

    if (mRequestQueue == null) {
      mRequestQueue = Volley.newRequestQueue(getApplicationContext());
    }
    return mRequestQueue;
  }

  public <T> void addToRequestQueue(Request<T> req, String tag) {
    Log.d(TAG, "addToRequestQueue");

    req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
    getRequestQueue().add(req);
  }

  public <T> void addToRequestQueue(Request<T> req) {
    Log.d(TAG, "addToRequestQueue");

    req.setTag(TAG);
    getRequestQueue().add(req);
  }

  public void cancelPendingRequests(Object tag) {
    Log.d(TAG, "cancelPendingRequests");

    if (mRequestQueue != null) {
      mRequestQueue.cancelAll(tag);
    }
  }
}
