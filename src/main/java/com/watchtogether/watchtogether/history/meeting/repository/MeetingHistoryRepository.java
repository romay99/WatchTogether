package com.watchtogether.watchtogether.history.meeting.repository;

import com.watchtogether.watchtogether.history.meeting.entity.WatchMeetingHistory;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingHistoryRepository extends JpaRepository<WatchMeetingHistory, Long> {

  boolean existsByMemberMemberIdAndMeetingCode(String memberId, Long meetingCode);

  @Query("SELECT wmh FROM WatchMeetingHistory wmh " +
      "JOIN FETCH wmh.meeting m " +
      "JOIN FETCH m.movie " +
      "JOIN FETCH m.cinema " +
      "WHERE wmh.member.memberId = :memberId")
  List<WatchMeetingHistory> findAllByMemberMemberIdWithFetch(String memberId, Pageable pageable);

}
