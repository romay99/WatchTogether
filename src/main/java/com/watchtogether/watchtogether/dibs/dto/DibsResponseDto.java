package com.watchtogether.watchtogether.dibs.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DibsResponseDto {
  private String username;
  private String movieTitle;
  private String dateTime;
}
