package com.watchtogether.watchtogether.movie.repository;

import com.watchtogether.watchtogether.movie.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
  Page<Movie> findAllByScreenAble(Pageable pageable,boolean screenAble);
}
