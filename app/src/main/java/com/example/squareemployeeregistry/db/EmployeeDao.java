package com.example.squareemployeeregistry.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface EmployeeDao {

  @Insert(onConflict = REPLACE)
  void insertEmployeeList(Employee... employeeList);

  @Query("SELECT * FROM Employee ORDER BY full_name desc")
  List<Employee> fetchAllEmployee();


  @Query("SELECT * FROM Employee WHERE uuid =:uuid")
  LiveData<Employee> getEmployee(int uuid);


  @Update
  void updateEmployee(Employee employee);


  @Query("DELETE FROM employee")
  void truncate();
}
