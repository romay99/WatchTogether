package com.watchtogether.watchtogether.movie.contoller;

import com.watchtogether.watchtogether.movie.dto.MovieListPageDto;
import com.watchtogether.watchtogether.movie.entity.Movie;
import com.watchtogether.watchtogether.movie.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movie")
@Tag(name = "영화", description = "영화 관련 API 입니다.")
public class MovieController {

  private final MovieService movieService;

  /**
   * 영화 제목을 통해 영화들을 검색
   *
   * @param page  페이지 번호
   * @param title 영화 제목
   */
  @GetMapping("/list/title")
  @Operation(summary = "영화 리스트 검색(제목)", description = "영화 제목으로 영화 데이터들을 검색합니다.")
  public ResponseEntity<String> getMovieListFromApi(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam String title) {
    return movieService.getMovieListFromApi(page, title);
  }

  /**
   * TmdbApiService 에서 가져온 영화 데이터를 저장한다.
   *
   * @param movieId 영화 ID 값
   */
  @Operation(summary = "영화 상세조회", description = "영화 ID 값으로 영화 상세 정보를 조회합니다."
      + " DB 에 존재하지 않는 영화일땐 TMDB API 를 호출 -> 저장합니다.")
  @GetMapping("/detail")
  public ResponseEntity<Movie> getMovieDetail(@RequestParam Long movieId) {
    return ResponseEntity.ok(movieService.getMovieDetail(movieId));
  }

  /**
   * 영화 리스트 가져오는 메서드
   *
   * @param page 페이지 번호
   * @param size 페이지 사이즈
   * @param able true / false 상영 가능 여부
   */
  @GetMapping("/list/screen")
  @Operation(summary = "영화 리스트 검색(상영 가능 여부)", description = "영화의 상영 가능 여부로 영화들을 조회합니다.")
  public ResponseEntity<MovieListPageDto> getMovieListByScreenAble(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "20") int size,
      @RequestParam boolean able) {
    MovieListPageDto dto = movieService.getMovieListByScreenAble(page, size, able);
    return ResponseEntity.ok(dto);
  }
}
