package com.example.squareemployeeregistry.viewmodel;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.squareemployeeregistry.db.Employee;
import com.example.squareemployeeregistry.db.EmployeeRepository;

import java.util.List;

public class MainActivityViewModel extends ViewModel {
  private MutableLiveData<List<Employee>> mEmployeeList;
  private Context mContext;

  public void init(Context context) {
    mEmployeeList = new MutableLiveData<>();
    mContext = context;
  }

  public MutableLiveData<List<Employee>> getEmployeeLiveData() {
    return mEmployeeList;
  }

  public void getEmployeeList() {
    final EmployeeRepository employeeRepository = new EmployeeRepository(mContext);
    AsyncTask.execute(new Runnable() {
      @Override
      public void run() {
        mEmployeeList.postValue(employeeRepository.getAllEmployees());
      }
    });
  }

}
