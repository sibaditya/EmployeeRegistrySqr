package com.example.squareemployeeregistry.db;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.util.List;

public class EmployeeRepository {

  private final String DB_NAME = "employee";
  private EmployeeDatabase employeeDatabase;

  public EmployeeRepository(Context context) {
    employeeDatabase = Room.databaseBuilder(context, EmployeeDatabase.class, DB_NAME).build();
  }

  /**
   * Queries for a list of Employees
   */
  public List<Employee> getAllEmployees() {
    if (employeeDatabase == null) {
      return null;
    }
    List<Employee> employeeList = employeeDatabase.employeeDaoAccess().fetchAllEmployee();
    return employeeList;
  }

  /**
   * Insert Employees into the DB.
   */
  public void insertEmployeeList(Employee... employeeList) {
    if (employeeDatabase == null) {
      return;
    }
    employeeDatabase.employeeDaoAccess().insertEmployeeList(employeeList);
  }

  /**
   * Clears the Employee table.
   */
  public void truncate() {
    if (employeeDatabase == null) {
      return;
    }
    employeeDatabase.employeeDaoAccess().truncate();
  }
}
