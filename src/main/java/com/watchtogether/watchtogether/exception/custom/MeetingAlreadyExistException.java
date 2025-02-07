package com.watchtogether.watchtogether.exception.custom;

public class MeetingAlreadyExistException extends RuntimeException {

  public MeetingAlreadyExistException(String message) {
    super(message);
  }
}
