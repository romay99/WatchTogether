package com.watchtogether.watchtogether.movie.repository;

import com.watchtogether.watchtogether.movie.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
  Page<Movie> findAllByScreenAble(Pageable pageable,boolean screenAble);

  @Modifying
  @Transactional
  @Query("UPDATE Movie m SET m.screenAble = true WHERE m.code = :movieCode")
  void updateScreenAbleTrue(@Param("movieCode") Long movieCode);
}
