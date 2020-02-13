package com.example.squareemployeeregistry.utils;

import java.io.Serializable;

public enum ResultCode implements Serializable {
  HAPPY_PATH(0,"Happy Path"),
  NETWORK_NOT_AVAILABLE(1, "The request is invalid"),
  MALFORMED_EMPLOYEE(2, "Malformed Employee found. Missing required fields"),
  EMPTY_EMPLOYEE_LIST(3, "Sorry!!! No Employee Found"),
  DEFAULT_ERROR(4, "Something went wrong, please try again!");

  private final int id;
  private final String msg;

  ResultCode(int id, String msg) {
    this.id = id;
    this.msg = msg;
  }

  public int getId() {
    return this.id;
  }

  public String getMsg() {
    return this.msg;
  }
}