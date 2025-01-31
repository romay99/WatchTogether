package com.watchtogether.watchtogether.cinema.dto;

import com.watchtogether.watchtogether.cinema.entity.Cinema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CinemaDto {

  private String name;
  private String description;
  private Double longitude;
  private Double latitude;

  static public CinemaDto toDto(Cinema cinema) {
    return CinemaDto.builder()
        .name(cinema.getName())
        .description(cinema.getDescription())
        .longitude(cinema.getCoordinates().getX())
        .latitude(cinema.getCoordinates().getY())
        .build();
  }
}
