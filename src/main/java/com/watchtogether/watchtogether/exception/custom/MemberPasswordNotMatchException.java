package com.watchtogether.watchtogether.exception.custom;

public class MemberPasswordNotMatchException extends RuntimeException {

  public MemberPasswordNotMatchException(String message) {
    super(message);
  }
}
