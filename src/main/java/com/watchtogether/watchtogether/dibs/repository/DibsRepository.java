package com.watchtogether.watchtogether.dibs.repository;

import com.watchtogether.watchtogether.dibs.entity.Dibs;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DibsRepository extends JpaRepository<Dibs, Long> {

  @Query("SELECT d FROM Dibs d WHERE d.member.memberId = :memberId")
  Page<Dibs> findAllByMemberMemberId(String memberId, Pageable pageable);

  @Query("SELECT d FROM Dibs d WHERE d.member.memberId = :memberId AND d.movie.code = :movieCode")
  Optional<Dibs> findByMemberIdAndMovieCode(String memberId, Long movieCode);

}
