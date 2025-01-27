package com.watchtogether.watchtogether.exception.custom;

public class MemberNotFoundException extends RuntimeException {

  public MemberNotFoundException(String message) {
    super(message);
  }
}
