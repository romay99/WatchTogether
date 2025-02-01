package com.watchtogether.watchtogether.movie.service;

import com.watchtogether.watchtogether.api.TmdbApiService;
import com.watchtogether.watchtogether.movie.dto.MovieIdNameDateDto;
import com.watchtogether.watchtogether.movie.dto.MovieListPageDto;
import com.watchtogether.watchtogether.movie.entity.Movie;
import com.watchtogether.watchtogether.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieService {

  private final MovieRepository movieRepository;
  private final TmdbApiService tmdbApiService;

  /**
   * 영화 제목으로 영화 목록 가져오기
   *
   * @param page  페이지 번호
   * @param title 영화 제목
   * @return 영화데이터들의 List 리턴
   */
  public ResponseEntity<String> getMovieListFromApi(int page, String title) {
    return tmdbApiService.getMovieList(page,title);
  }

  /**
   * TmdbApiService 에서 가져온 영화 데이터를 저장한다.
   *
   * @param movieId 영화 ID 값
   */
  public Movie getMovieDetail(Long movieId) {
    return movieRepository.findById(movieId)
        .orElseGet(() -> movieRepository.save(tmdbApiService.findMovieById(movieId)));
  }

  /**
   * 영화 리스트를 가져오는 메서드
   *
   * @param page 페이지 번호
   * @param size 페이지 사이즈
   * @param able true / false 상영 가능여부
   */
  public MovieListPageDto getMovieListByScreenAble(int page, int size, boolean able) {
    // 입력받은 페이징 정보 설정
    Pageable pageable = PageRequest.of(--page, size);
    Page<Movie> list = movieRepository.findAllByScreenAble(pageable, able);

    return MovieListPageDto.builder()
        .totalElements(list.getTotalElements())
        .totalPageNumber(list.getTotalPages())
        .data(list.stream().map((movie) -> MovieIdNameDateDto.toDto(movie)).toList())
        .build();
  }
}
