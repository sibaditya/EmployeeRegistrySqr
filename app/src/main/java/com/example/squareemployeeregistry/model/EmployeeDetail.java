package com.example.squareemployeeregistry.model;

import com.google.gson.annotations.SerializedName;

public class EmployeeDetail {
  @SerializedName("uuid")
  String uuid;

  @SerializedName("full_name")
  String fullName;

  @SerializedName("phone_number")
  long phoneNumber;

  @SerializedName("email_address")
  String emailAddress;

  @SerializedName("biography")
  String biography;

  @SerializedName("photo_url_small")
  String photoUrlSmall;

  @SerializedName("photo_url_large")
  String photoUrlLarge;

  @SerializedName("team")
  String team;

  @SerializedName("employee_type")
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
}
