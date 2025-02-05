package com.watchtogether.watchtogether.movie.dto;

import com.watchtogether.watchtogether.movie.entity.Movie;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MovieIdNameDateDto {

  private Long id;
  private String title;
  private String date;
  private String posterUrl;

  public static MovieIdNameDateDto toDto(Movie movie) {
    return MovieIdNameDateDto.builder()
        .id(movie.getCode())
        .title(movie.getTitle())
        .date(movie.getReleaseDate().toString())
        .posterUrl(movie.getPosterUrl())
        .build();
  }
}
