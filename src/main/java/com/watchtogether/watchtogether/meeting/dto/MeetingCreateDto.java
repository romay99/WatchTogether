package com.watchtogether.watchtogether.meeting.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeetingCreateDto {
  private Long cinemaId;
  private Long movieId;
  private LocalDateTime time;

}
