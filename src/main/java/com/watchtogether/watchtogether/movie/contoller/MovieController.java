package com.watchtogether.watchtogether.movie.contoller;

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

  @GetMapping("/list")
  public ResponseEntity<String> getMovieListFromApi(@RequestParam String title) {
    return movieService.getMovieListFromApi(title);
  }
}
