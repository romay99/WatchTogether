package com.watchtogether.watchtogether.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
  private String errorMessage;

  public ErrorResponse(String errorMessage) {
    this.errorMessage = errorMessage;
  }
}