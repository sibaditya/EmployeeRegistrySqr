package com.example.squareemployeeregistry.viewmodel;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.squareemployeeregistry.db.Employee;
import com.example.squareemployeeregistry.db.EmployeeRepository;
import com.example.squareemployeeregistry.model.EmployeeDetail;
import com.example.squareemployeeregistry.model.Employees;
import com.example.squareemployeeregistry.repository.ConnectivityManager;
import com.example.squareemployeeregistry.utils.ResultCode;
import com.example.squareemployeeregistry.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

public class SplashScreenViewModel extends ViewModel implements ConnectivityManager.FetchNetworkResponse {

  private Context mContext;
  private MutableLiveData<Employees> mEmployeesMutableLiveData;
  private MutableLiveData<ResultCode> mErrorMessageCode;

  public void init(Context context) {
    mContext = context;
    mEmployeesMutableLiveData = new MutableLiveData<>();
    mErrorMessageCode = new MutableLiveData<>();
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
      mErrorMessageCode.postValue(ResultCode.NETWORK_NOT_AVAILABLE);
    }
  }

  public LiveData<ResultCode> getErrorMessageLiveData() {
    return mErrorMessageCode;
  }

  @Override
  public <T> void onSuccess(List<T> modelList) {
    ArrayList<Employees> employeesArrayList = (ArrayList<Employees>)modelList;
    if (employeesArrayList != null && employeesArrayList.size() > 0) {
      List<EmployeeDetail> employeeDetailArrayList = employeesArrayList.get(0).getEmployees();
      if (employeeDetailArrayList.isEmpty()) {
        mErrorMessageCode.postValue(ResultCode.EMPTY_EMPLOYEE_LIST);
        return;
      }
      mEmployeesMutableLiveData.postValue(employeesArrayList.get(0));
      final EmployeeRepository employeeRepository = new EmployeeRepository(mContext);
      final Employee[] employeeList = new Employee[employeeDetailArrayList.size()];
      for (int i = 0; i < employeesArrayList.get(0).getEmployees().size(); i++) {
        if(isValidEmployee(employeeDetailArrayList.get(i))) {
          employeeList[i]  = getEmployee(employeeDetailArrayList.get(i));
        } else {
          mErrorMessageCode.postValue(ResultCode.MALFORMED_EMPLOYEE);
          return;
        }
      }
      AsyncTask.execute(new Runnable() {
        @Override
        public void run() {
          employeeRepository.insertEmployeeList(employeeList);
        }
      });
    }
  }

  private Employee getEmployee(EmployeeDetail employeeDetail) {
    Employee employee = new Employee();
    employee.setBiography(employeeDetail.getBiography());
    employee.setUuid(employeeDetail.getUuid());
    employee.setFullName(employeeDetail.getFullName());
    employee.setEmailAddress(employeeDetail.getEmailAddress());
    employee.setEmployeeType(employeeDetail.getEmployeeType());
    employee.setPhoneNumber(employeeDetail.getPhoneNumber());
    employee.setPhotoUrlLarge(employeeDetail.getPhotoUrlLarge());
    employee.setPhotoUrlSmall(employeeDetail.getPhotoUrlSmall());
    employee.setTeam(employeeDetail.getTeam());
    return employee;
  }

  private boolean isValidEmployee(EmployeeDetail employeeDetail) {
    if (!TextUtils.isEmpty(employeeDetail.getUuid())
            && !TextUtils.isEmpty(employeeDetail.getFullName())
            && !TextUtils.isEmpty(employeeDetail.getEmailAddress())
            && !TextUtils.isEmpty(employeeDetail.getTeam())
            && !TextUtils.isEmpty(employeeDetail.getEmployeeType())) {
      return true;
    }
    return false;
  }

  @Override
  public void onFailure(String msg) {
    mErrorMessageCode.postValue(ResultCode.DEFAULT_ERROR);
  }
}
