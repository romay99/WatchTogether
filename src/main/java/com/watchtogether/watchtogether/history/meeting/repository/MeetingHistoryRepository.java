package com.watchtogether.watchtogether.history.meeting.repository;

import com.watchtogether.watchtogether.history.meeting.entity.WatchMeetingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingHistoryRepository extends JpaRepository<WatchMeetingHistory, Long> {


}
