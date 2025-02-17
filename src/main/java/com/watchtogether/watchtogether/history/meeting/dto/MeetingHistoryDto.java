package com.watchtogether.watchtogether.history.meeting.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MeetingHistoryDto {
  private LocalDateTime dateTime;
  private LocalDateTime canceledDateTime;
  private String movieTitle;
  private String cinemaName;

}
