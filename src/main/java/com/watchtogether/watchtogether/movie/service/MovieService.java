package com.watchtogether.watchtogether.movie.service;

import com.watchtogether.watchtogether.api.TmdbApiService;
import com.watchtogether.watchtogether.movie.entity.Movie;
import com.watchtogether.watchtogether.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
   * @param title 영화 제목
   * @return 영화데이터들의 List 리턴
   */
  public ResponseEntity<String> getMovieListFromApi(String title) {
    return tmdbApiService.getMovieList(title);
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
}
