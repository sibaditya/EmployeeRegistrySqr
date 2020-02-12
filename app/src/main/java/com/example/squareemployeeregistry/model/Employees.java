package com.example.squareemployeeregistry.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Employees {

  @SerializedName("employees")
  List<EmployeeDetail> employees;

  public List<EmployeeDetail> getEmployees() {
    return employees;
  }
}
