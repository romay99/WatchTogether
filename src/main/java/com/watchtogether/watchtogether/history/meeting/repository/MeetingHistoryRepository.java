package com.watchtogether.watchtogether.history.meeting.repository;

import com.watchtogether.watchtogether.history.meeting.entity.WatchMeetingHistory;
import com.watchtogether.watchtogether.meeting.entity.WatchMeeting;
import com.watchtogether.watchtogether.member.entity.Member;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

  Optional<WatchMeetingHistory> findByMemberAndMeeting(Member member, WatchMeeting meeting);

  @Modifying
  @Query("UPDATE WatchMeetingHistory h SET h.cancelDateTime = :dateTime WHERE h.code = :watchMeetingHistoryId")
  void updateCancelDateTime(LocalDateTime dateTime,Long watchMeetingHistoryId);
}
