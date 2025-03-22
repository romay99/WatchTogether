package com.watchtogether.watchtogether.movie.service;

import com.watchtogether.watchtogether.api.service.TmdbApiService;
import com.watchtogether.watchtogether.dibs.repository.DibsRepository;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieService {

  private final MovieRepository movieRepository;
  private final TmdbApiService tmdbApiService;
  private final DibsRepository dibsRepository;

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

  /**
   * 독립된 트랜잭션에서 영화 데이터를 저장할때 필요한 메서드
   * @param movie 저장할 영화 정보
   * @return 저장된 영화 정보
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Movie saveMovie(Movie movie) {
    return movieRepository.save(movie);
  }

//  @Scheduled(cron = "0 * * * * *") // 매 분 0초마다 실행
  @Transactional
  public void updateMovieScreenAble() {
    log.info("실행");

    Page<Movie> allFalseMovies = movieRepository // 실행 여부가 "불가능" 인 영화들 전부조회
        .findAllByScreenAble(Pageable.unpaged(), false);

    allFalseMovies.getContent().stream().forEach(movie -> {
      int result = dibsRepository.countDibsByMovieCode(movie.getCode()); // 찜갯수 카운트
      if (result > 0) {
        movieRepository.updateScreenAbleTrue(movie.getCode());
        log.info("{} 영화가 상영가능으로 변경되었습니다.", movie.getTitle());
        dibsRepository.deleteDibsByMovieCode(movie.getCode());
        log.info("찜 삭제완료");
      }
    });
  }
}
