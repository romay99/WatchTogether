package com.watchtogether.watchtogether.movie.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MovieListPageDto {
  private Long totalElements;
  private Integer totalPageNumber;
  private List<MovieIdNameDateDto> data;
}
