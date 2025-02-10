package com.watchtogether.watchtogether.exception.custom;

public class MeetingNotFoundException extends RuntimeException{

  public MeetingNotFoundException(String message) {
    super(message);
  }
}
