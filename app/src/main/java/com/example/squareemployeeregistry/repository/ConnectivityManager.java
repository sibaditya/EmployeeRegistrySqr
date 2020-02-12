package com.example.squareemployeeregistry.repository;

import android.content.Context;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.squareemployeeregistry.Application.AppController;
import com.example.squareemployeeregistry.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectivityManager {


  /**
   * Interface for callback
   */
  public interface FetchNetworkResponse {
    <T> void onSuccess(List<T> modelList);

    void onFailure(String msg);
  }

  /**
   * TAG for logs
   */
  private final String TAG = getClass().getSimpleName();

  private Gson gson;

  /**
   * {@link FetchNetworkResponse} callback listener object
   */
  private FetchNetworkResponse dataResponse;

  /**
   * Context
   */
  private Context mContext;

  public ConnectivityManager(FetchNetworkResponse dataResponse, Context context) {
    this.dataResponse = dataResponse;
    this.mContext = context;
  }

  /**
   * Method to fetch feed from the network
   */
  public <T> void fetchFeedFromNetwork(String url, final Class<T> modelClass) {
    Log.d(TAG, "fetchFeedFromNetwork");
    Log.d(TAG,"url = "+url);
    // Instantiate the RequestQueue.
    RequestQueue queue = Volley.newRequestQueue(mContext);
    // Request a string response from the provided URL.
    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
              @Override
              public void onResponse(String response) {
                Log.d(TAG, "on Success");
                handleResponse(response, modelClass);
              }
            }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        dataResponse.onFailure(mContext.getString(R.string.something_went_wrong));
      }
    } )
    {
      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String>  params = new HashMap<String, String>();
        return params;
      }

      @Override
      protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        return params;
      }
    };

    // Add the request to the RequestQueue.
    Log.d("StringRequest", stringRequest.getUrl());
    AppController.getInstance().addToRequestQueue(stringRequest);
  }

  /**
   * Method to handle the response data either fetched from network or cache
   *
   * @param response String
   */
  private <T> void handleResponse(String response, Class<T> modelClass) {
    Log.d(TAG, "handle response");
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.setDateFormat("M/d/yy hh:mm a");
    gson = gsonBuilder.create();
    List<T> modelList = new ArrayList<>(Arrays.asList(gson.fromJson(response, modelClass)));
    dataResponse.onSuccess(modelList);
  }

  /**
   * Method to check the availability of internet
   *
   * @param context {@link Context}
   * @return boolean
   */
  public static boolean isInternetAvailable(Context context) {
    android.net.ConnectivityManager cm =
            (android.net.ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    assert cm != null;
    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
  }

}
