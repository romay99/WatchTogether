package com.watchtogether.watchtogether.meeting.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MeetingJoinResponseDto {
  private String message;
  private LocalDateTime meetingDateTime;
  private LocalDateTime joinDateTime;
  private String cinemaName;

}
