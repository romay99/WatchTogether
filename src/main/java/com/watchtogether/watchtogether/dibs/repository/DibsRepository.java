package com.watchtogether.watchtogether.dibs.repository;

import com.watchtogether.watchtogether.dibs.entity.Dibs;
import jakarta.persistence.Tuple;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface DibsRepository extends JpaRepository<Dibs, Long> {

  Page<Dibs> findAllByMemberMemberId(String memberId, Pageable pageable);

  @Modifying
  @Query("DELETE FROM Dibs d WHERE d.member.memberId = :memberId AND d.movie.code = :movieCode")
  int deleteDibs(String memberId, Long movieCode);

  @Query("SELECT m, mo, CASE WHEN COUNT(d) > 0 THEN true ELSE false END " +
      "FROM Member m " +
      "JOIN Movie mo ON mo.code = :movieId " +
      "LEFT JOIN Dibs d ON d.member = m AND d.movie = mo " +
      "WHERE m.memberId = :memberId")
  Optional<Tuple> findMovieAndMemberAndCheckDibs(Long movieId, String memberId);
}
