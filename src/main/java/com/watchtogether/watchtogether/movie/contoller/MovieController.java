package com.watchtogether.watchtogether.movie.contoller;

import com.watchtogether.watchtogether.movie.entity.Movie;
import com.watchtogether.watchtogether.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movie")
public class MovieController {

  private final MovieService movieService;

  /**
   * 영화 제목을 통해 영화들을 검색
   *
   * @param title 영화 제목
   * @return 영화 데이터들의 List
   */
  @GetMapping("/list")
  public ResponseEntity<String> getMovieListFromApi(@RequestParam String title) {
    return movieService.getMovieListFromApi(title);
  }

  /**
   * TmdbApiService 에서 가져온 영화 데이터를 저장한다.
   *
   * @param movieId 영화 ID 값
   */
  @GetMapping("/detail")
  public ResponseEntity<Movie> getMovieDetail(@RequestParam Long movieId) {
    return ResponseEntity.ok(movieService.getMovieDetail(movieId));
  }
}
