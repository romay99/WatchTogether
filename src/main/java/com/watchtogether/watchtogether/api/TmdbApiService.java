package com.watchtogether.watchtogether.api;

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
}
