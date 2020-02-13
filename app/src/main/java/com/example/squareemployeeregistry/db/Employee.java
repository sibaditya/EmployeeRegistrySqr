package com.example.squareemployeeregistry.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "employee")
public class Employee implements Serializable {
  @PrimaryKey
  @ColumnInfo(name = "uuid")
  @NonNull
  String uuid;

  @NonNull
  @ColumnInfo(name = "full_name")
  String fullName;

  @ColumnInfo(name = "phone_number")
  long phoneNumber;

  @NonNull
  @ColumnInfo(name = "email_address")
  String emailAddress;

  @ColumnInfo(name = "biography")
  String biography;

  @ColumnInfo(name = "photo_url_small")
  String photoUrlSmall;

  @ColumnInfo(name = "photo_url_large")
  String photoUrlLarge;

  @NonNull
  @ColumnInfo(name = "team")
  String team;

  @NonNull
  @ColumnInfo(name = "employee_type")
  String employeeType;

  public String getUuid() {
    return uuid;
  }

  public String getFullName() {
    return fullName;
  }

  public long getPhoneNumber() {
    return phoneNumber;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public String getBiography() {
    return biography;
  }

  public String getPhotoUrlSmall() {
    return photoUrlSmall;
  }

  public String getPhotoUrlLarge() {
    return photoUrlLarge;
  }

  public String getTeam() {
    return team;
  }

  public String getEmployeeType() {
    return employeeType;
  }


  public void setUuid(@NonNull String uuid) {
    this.uuid = uuid;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public void setPhoneNumber(long phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public void setBiography(String biography) {
    this.biography = biography;
  }

  public void setPhotoUrlSmall(String photoUrlSmall) {
    this.photoUrlSmall = photoUrlSmall;
  }

  public void setPhotoUrlLarge(String photoUrlLarge) {
    this.photoUrlLarge = photoUrlLarge;
  }

  public void setTeam(String team) {
    this.team = team;
  }

  public void setEmployeeType(String employeeType) {
    this.employeeType = employeeType;
  }
}
