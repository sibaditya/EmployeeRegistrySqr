package com.example.squareemployeeregistry.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Employee.class}, version = 1, exportSchema = false)
public abstract class EmployeeDatabase extends RoomDatabase {
  public abstract EmployeeDao employeeDaoAccess();
}
