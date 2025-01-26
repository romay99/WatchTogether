package com.watchtogether.watchtogether.exception;

import com.watchtogether.watchtogether.exception.custom.MemberIdAlreadyUseException;
import com.watchtogether.watchtogether.exception.custom.MovieDataNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * 이미 사용중인 ID 에 대한 예외 처리
   */
  @ExceptionHandler(MemberIdAlreadyUseException.class)
  public ResponseEntity<ErrorResponse> handleIdAlreadyUsedException(MemberIdAlreadyUseException e) {
    ErrorResponse response = new ErrorResponse(e.getMessage());
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  /**
   * 영화 데이터가 존재하지 않을때 예외 처리
   */
  @ExceptionHandler(MovieDataNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleMovieDataNotFoundException(
      MovieDataNotFoundException e) {
    ErrorResponse response = new ErrorResponse(e.getMessage());
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

}
