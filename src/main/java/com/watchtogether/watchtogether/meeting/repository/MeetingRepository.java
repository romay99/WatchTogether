package com.watchtogether.watchtogether.meeting.repository;

import com.watchtogether.watchtogether.meeting.entity.WatchMeeting;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingRepository extends JpaRepository<WatchMeeting, Long> {

  @Query("SELECT COUNT(m) > 0 FROM WatchMeeting m " +
      "WHERE m.cinema.code = :cinemaCode " +
      "AND m.movie.code = :movieCode " +
      "AND FUNCTION('DATE_FORMAT', m.dateTime, '%Y-%m-%d %H') = FUNCTION('DATE_FORMAT', :time, '%Y-%m-%d %H')")
  boolean existsCheck(Long cinemaCode, Long movieCode, LocalDateTime time);

}
