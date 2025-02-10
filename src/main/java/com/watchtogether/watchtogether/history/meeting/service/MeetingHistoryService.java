package com.watchtogether.watchtogether.history.meeting.service;

import com.watchtogether.watchtogether.history.meeting.entity.WatchMeetingHistory;
import com.watchtogether.watchtogether.history.meeting.repository.MeetingHistoryRepository;
import com.watchtogether.watchtogether.meeting.entity.WatchMeeting;
import com.watchtogether.watchtogether.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MeetingHistoryService {
  private final MeetingHistoryRepository meetingHistoryRepository;

  /**
   * 같이볼까요 신청기록 남기는 메서드
   * @param member 신청한 Member
   * @param meeting 신청한 같이볼까요 데이터
   */
  @Transactional
  public WatchMeetingHistory recordMeetingHistory(Member member, WatchMeeting meeting) {
    WatchMeetingHistory history = WatchMeetingHistory.builder()
        .meeting(meeting)
        .member(member)
        .build();
    return meetingHistoryRepository.save(history);
  }
}
