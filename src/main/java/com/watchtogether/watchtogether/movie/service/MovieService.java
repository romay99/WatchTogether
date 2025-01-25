package com.watchtogether.watchtogether.movie.service;

import com.watchtogether.watchtogether.api.TmdbApiService;
import com.watchtogether.watchtogether.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieService {

  private final MovieRepository movieRepository;
  private final TmdbApiService tmdbApiService;


  public ResponseEntity<String> getMovieListFromApi(String title) {
    return tmdbApiService.getMovieList(title);
  }
}
