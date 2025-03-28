package com.watchtogether.watchtogether.meeting.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MeetingCancelInfoDto {
  private String movieTitle;
  private LocalDateTime dateTime;
}
