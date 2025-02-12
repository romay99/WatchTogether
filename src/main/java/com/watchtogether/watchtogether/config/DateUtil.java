package com.watchtogether.watchtogether.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class DateUtil {
  /**
   * 같이볼까요 취소를 위해 날짜 비교하는 메서드
   *
   * @param dateTime 같이볼까요의 날짜
   * @param now      취소하려는 현재 날짜
   * @return true == 취소 가능 / false == 취소 불가능
   */
  public boolean checkDate(LocalDateTime dateTime, LocalDateTime now, int day) {
    // A 데이터에서 날짜만 추출
    LocalDate dateA = dateTime.toLocalDate();

    // B 데이터에서 날짜만 추출
    LocalDate dateB = now.toLocalDate();

    // 3일 차이 확인
    if (dateA.minusDays(day).isBefore(dateB)) {
      return false; // false = 불가능
    }
    return true; // 가능
  }
}


