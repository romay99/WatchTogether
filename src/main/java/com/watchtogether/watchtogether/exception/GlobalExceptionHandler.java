package com.watchtogether.watchtogether.exception;

import com.watchtogether.watchtogether.exception.custom.AlreadyDibsException;
import com.watchtogether.watchtogether.exception.custom.CinemaNotFoundException;
import com.watchtogether.watchtogether.exception.custom.DibsNotFoundException;
import com.watchtogether.watchtogether.exception.custom.MeetingAlreadyExistException;
import com.watchtogether.watchtogether.exception.custom.MeetingAlreadyRegisterException;
import com.watchtogether.watchtogether.exception.custom.MeetingCancelNotValidDateException;
import com.watchtogether.watchtogether.exception.custom.MeetingHistoryNotValidException;
import com.watchtogether.watchtogether.exception.custom.MeetingMaxPeopleException;
import com.watchtogether.watchtogether.exception.custom.MeetingNotFoundException;
import com.watchtogether.watchtogether.exception.custom.MemberIdAlreadyUseException;
import com.watchtogether.watchtogether.exception.custom.MemberNotEnoughPointException;
import com.watchtogether.watchtogether.exception.custom.MovieAlreadyScreenAbleException;
import com.watchtogether.watchtogether.exception.custom.MovieDataNotFoundException;
import com.watchtogether.watchtogether.exception.custom.MemberNotFoundException;
import com.watchtogether.watchtogether.exception.custom.MemberPasswordNotMatchException;
import com.watchtogether.watchtogether.exception.custom.MovieNotScreenAbleException;
import com.watchtogether.watchtogether.exception.custom.PartnerCanHaveOneCinemaException;
import com.watchtogether.watchtogether.exception.custom.RedissonClientLockedException;
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

  /**
   * 사용자가 존재하지 않을 때 예외 처리
   */
  @ExceptionHandler(MemberNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleMemberNotFoundException(MemberNotFoundException e) {
    ErrorResponse response = new ErrorResponse(e.getMessage());
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  /**
   * 사용자의 아이디와 비밀번호가 일치하지 않을때 예외처리
   */
  @ExceptionHandler(MemberPasswordNotMatchException.class)
  public ResponseEntity<ErrorResponse> handleMemberPasswordNotMatchException(
      MemberPasswordNotMatchException e) {
    ErrorResponse response = new ErrorResponse(e.getMessage());
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  /**
   * 하나의 파트너계정에 여러개의 극장을 등록하려할때 예외처리
   */
  @ExceptionHandler(PartnerCanHaveOneCinemaException.class)
  public ResponseEntity<ErrorResponse> handlePartnerCanHaveOneCinemaException(
      PartnerCanHaveOneCinemaException e) {
    ErrorResponse response = new ErrorResponse(e.getMessage());
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  /**
   * 극장 정보가 존재하지 않을때 예외처리
   */
  @ExceptionHandler(CinemaNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleCinemaNotFoundException(
      CinemaNotFoundException e) {
    ErrorResponse response = new ErrorResponse(e.getMessage());
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  /**
   * 상영 가능한 영화를 찜목록에 추가하려할때 예외처리
   */
  @ExceptionHandler(MovieAlreadyScreenAbleException.class)
  public ResponseEntity<ErrorResponse> handleMovieAlreadyScreenAbleException(
      MovieAlreadyScreenAbleException e) {
    ErrorResponse response = new ErrorResponse(e.getMessage());
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  /**
   * 이미 찜한 영화를 또 찜하려할때 예외처리
   */
  @ExceptionHandler(AlreadyDibsException.class)
  public ResponseEntity<ErrorResponse> handleAlreadyDibsException(
      AlreadyDibsException e) {
    ErrorResponse response = new ErrorResponse(e.getMessage());
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  /**
   * 찜 목록이 존재하지 않을때 예외처리
   */
  @ExceptionHandler(DibsNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleDibsNotFoundException(
      DibsNotFoundException e) {
    ErrorResponse response = new ErrorResponse(e.getMessage());
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  /**
   * 상영 불가능한 영화로 같이볼까요? 생성할때 예외
   */
  @ExceptionHandler(MovieNotScreenAbleException.class)
  public ResponseEntity<ErrorResponse> handleMovieNotScreenAbleException(
      MovieNotScreenAbleException e) {
    ErrorResponse response = new ErrorResponse(e.getMessage());
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  /**
   * 사용자 포인트가 충분하지 않을 때 예외처리
   */
  @ExceptionHandler(MemberNotEnoughPointException.class)
  public ResponseEntity<ErrorResponse> handleMemberNotEnoughPointException(
      MemberNotEnoughPointException e) {
    ErrorResponse response = new ErrorResponse(e.getMessage());
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  /**
   * 이미 존재하는 시간대의 같이볼까요 일때 예외처리
   */
  @ExceptionHandler(MeetingAlreadyExistException.class)
  public ResponseEntity<ErrorResponse> handleMeetingAlreadyExistException(
      MeetingAlreadyExistException e) {
    ErrorResponse response = new ErrorResponse(e.getMessage());
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  /**
   * 같이볼까요 중복신청 예외처리
   */
  @ExceptionHandler(MeetingAlreadyRegisterException.class)
  public ResponseEntity<ErrorResponse> handleMeetingAlreadyRegisterException(
      MeetingAlreadyRegisterException e) {
    ErrorResponse response = new ErrorResponse(e.getMessage());
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  /**
   * 같이볼까요 인원초과 예외처리
   */
  @ExceptionHandler(MeetingMaxPeopleException.class)
  public ResponseEntity<ErrorResponse> handleMeetingMaxPeopleException(
      MeetingMaxPeopleException e) {
    ErrorResponse response = new ErrorResponse(e.getMessage());
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  /**
   * 같이볼까요 데이터가 존재하지 않을때 예외처리
   */
  @ExceptionHandler(MeetingNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleMeetingNotFoundException(
      MeetingNotFoundException e) {
    ErrorResponse response = new ErrorResponse(e.getMessage());
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  /**
   * Redisson Lock 획득 실패시 예외처리
   */
  @ExceptionHandler(RedissonClientLockedException.class)
  public ResponseEntity<ErrorResponse> handleRedissonClientLockedException(
      RedissonClientLockedException e) {
    ErrorResponse response = new ErrorResponse(e.getMessage());
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  /**
   * 같이볼까요를 취소할때 취소할 같이볼까요가 존재하지 않을때 예외 처리
   */
  @ExceptionHandler(MeetingHistoryNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMeetingHistoryNotValidException(
      MeetingHistoryNotValidException e) {
    ErrorResponse response = new ErrorResponse(e.getMessage());
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  /**
   * 같이볼까요를 취소할때 취소할 같이볼까요의 날짜가 3일 이내일때 예외처리
   */
  @ExceptionHandler(MeetingCancelNotValidDateException.class)
  public ResponseEntity<ErrorResponse> handleMeetingCancelNotValidDateException(
      MeetingCancelNotValidDateException e) {
    ErrorResponse response = new ErrorResponse(e.getMessage());
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }





  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResponse> handleException(RuntimeException e) {
    ErrorResponse response = new ErrorResponse(e.getMessage());
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
