package com.watchtogether.watchtogether.exception.custom;

public class RedissonClientLockedException extends RuntimeException {

  public RedissonClientLockedException(String message) {
    super(message);
  }
}
