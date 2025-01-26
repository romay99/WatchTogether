package com.watchtogether.watchtogether.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.watchtogether.watchtogether.movie.entity.Movie;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class TmdbApiService {

  private final String API_KEY;
  private final String rootUri = "https://api.themoviedb.org/3";
  private final RestTemplate restTemplate;

  // API KEY 를 application.properties 에서 가져온다.
  public TmdbApiService(@Value("${tmdb.apiKey}") String key, RestTemplateBuilder builder) {
    this.API_KEY = key;
    this.restTemplate = builder.build();
  }

  /**
   * 영화 제목으로 영화 목록 가져오기
   *
   * @param title 영화제목
   * @return 영화들의 List 리턴
   */
  public ResponseEntity<String> getMovieList(String title) {
    // API 요청을 위한 URI 생성
    URI uri = UriComponentsBuilder.fromUriString(rootUri)
        .path("/search/movie")
        .queryParam("query", title)
        .queryParam("include_adult", "true")
        .queryParam("language", "ko-KR")
        .encode().build().toUri();

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + API_KEY);
    HttpEntity<String> entity = new HttpEntity<>(headers);

    log.info("GET : " + uri);

    return restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
  }

  /**
   * 영화 ID 값으로 상세정보 가져오기
   *
   * @param id 영화 PK
   * @return Movie entity
   */
  public Movie findMovieById(Long id) {
    // API 요청을 위한 URI 생성
    URI uri = UriComponentsBuilder.fromUriString(rootUri)
        .path("/movie/" + id)
        .queryParam("language", "ko-KR")
        .encode().build().toUri();

    // Header 에 API KEY 설정
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + API_KEY);
    HttpEntity<String> entity = new HttpEntity<>(headers);

    log.info("GET : " + uri);

    ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity,
        String.class);

    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    // 받아온 String 형식의 데이터를 Movie entity 로 변환
    Movie movie = null;
    try {
      movie = mapper.readValue(response.getBody(), Movie.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    log.info(movie.getTitle() + " 데이터 가져오기 완료");

    return movie;
  }
}
