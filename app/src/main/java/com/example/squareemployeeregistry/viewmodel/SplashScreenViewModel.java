package com.example.squareemployeeregistry.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.squareemployeeregistry.R;
import com.example.squareemployeeregistry.model.Employees;
import com.example.squareemployeeregistry.repository.ConnectivityManager;
import com.example.squareemployeeregistry.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

public class SplashScreenViewModel extends ViewModel implements ConnectivityManager.FetchNetworkResponse {

  private Context mContext;
  private MutableLiveData<Employees> mEmployeesMutableLiveData;
  private MutableLiveData<String> mErrorMessage;

  public void init(Context context) {
    mContext = context;
    mEmployeesMutableLiveData = new MutableLiveData<>();
    mErrorMessage = new MutableLiveData<>();
  }

  public LiveData<Employees> getEmployeeListLiveData() {
    if (mEmployeesMutableLiveData == null) {
      mEmployeesMutableLiveData = new MutableLiveData<>();
    }
    return mEmployeesMutableLiveData;
  }

  public void getEmployeeList() {
    if (ConnectivityManager.isInternetAvailable(mContext)) {
      ConnectivityManager connectivityManager = new ConnectivityManager(this, mContext);
      connectivityManager.fetchFeedFromNetwork(UrlUtils.EMPLOYEE_LIST_SUCCESS_URL, Employees.class);
    } else {
      mErrorMessage.postValue(mContext.getString(R.string.internet_not_available));
    }
  }

  public LiveData<String> getErrorMessageLiveData() {
    return mErrorMessage;
  }

  @Override
  public <T> void onSuccess(List<T> modelList) {
    ArrayList<Employees> employeesArrayList = (ArrayList<Employees>)modelList;
    if (employeesArrayList != null && employeesArrayList.size() > 0) {
      mEmployeesMutableLiveData.postValue(employeesArrayList.get(0));
    }
  }

  @Override
  public void onFailure(String msg) {
    mErrorMessage.postValue(msg);
  }
}
