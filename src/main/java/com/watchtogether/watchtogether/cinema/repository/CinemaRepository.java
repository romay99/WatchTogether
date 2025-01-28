package com.watchtogether.watchtogether.cinema.repository;

import com.watchtogether.watchtogether.cinema.entity.Cinema;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Long> {

  Optional<Cinema> findByMemberMemberId(String memberId);

  void deleteByMemberMemberId(String memberId);

}
