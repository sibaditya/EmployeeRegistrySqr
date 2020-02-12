package com.example.squareemployeeregistry.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.squareemployeeregistry.R;
import com.example.squareemployeeregistry.model.Employees;
import com.example.squareemployeeregistry.viewmodel.SplashScreenViewModel;


public class SplashActivity extends AppCompatActivity {
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
    SplashScreenViewModel splashScreenViewModel = ViewModelProviders.of(this).get(SplashScreenViewModel.class);
    splashScreenViewModel.init(this);
    splashScreenViewModel.getEmployeeList();
    splashScreenViewModel.getEmployeeListLiveData().observe(this, new Observer<Employees>(){
      @Override
      public void onChanged(Employees employees) {
        Intent i = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(i);
        finish();
      }
    });

    splashScreenViewModel.getErrorMessageLiveData().observe(this, new Observer<String>() {
      @Override
      public void onChanged(String s) {
        Toast.makeText(SplashActivity.this, s, Toast.LENGTH_LONG).show();
      }
    });
  }

}
