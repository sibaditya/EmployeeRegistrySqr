package com.example.squareemployeeregistry.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.squareemployeeregistry.R;
import com.example.squareemployeeregistry.model.Employees;
import com.example.squareemployeeregistry.utils.ResultCode;
import com.example.squareemployeeregistry.viewmodel.SplashScreenViewModel;


public class SplashActivity extends AppCompatActivity {

  public static String RESULT_CODE_BUNDLE_VALUE = "RESULT_CODE_BUNDLE_VALUE";

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
    SplashScreenViewModel splashScreenViewModel = ViewModelProviders.of(this).get(SplashScreenViewModel.class);
    splashScreenViewModel.init(this);
    splashScreenViewModel.getEmployeeListFromNetwork();
    splashScreenViewModel.getEmployeeListLiveData().observe(this, new Observer<Employees>(){
      @Override
      public void onChanged(Employees employees) {
        if(!employees.getEmployees().isEmpty()) {
          launchMainActivity(ResultCode.HAPPY_PATH);
        } else {
          launchMainActivity(ResultCode.EMPTY_EMPLOYEE_LIST);
        }
      }
    });

    splashScreenViewModel.getErrorMessageLiveData().observe(this, new Observer<ResultCode>() {
      @Override
      public void onChanged(ResultCode resultCode) {
        launchMainActivity(resultCode);
      }
    });
  }

  private void launchMainActivity(ResultCode code) {
    Intent mainActivityIntent = new Intent(SplashActivity.this, MainActivity.class);
    mainActivityIntent.putExtra(RESULT_CODE_BUNDLE_VALUE, code);
    startActivity(mainActivityIntent);
    finish();
  }

}
