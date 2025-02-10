package com.watchtogether.watchtogether.history.meeting.service;

import com.watchtogether.watchtogether.history.meeting.entity.WatchMeetingHistory;
import com.watchtogether.watchtogether.history.meeting.repository.MeetingHistoryRepository;
import com.watchtogether.watchtogether.meeting.entity.WatchMeeting;
import com.watchtogether.watchtogether.member.entity.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MeetingHistoryService {

  private final MeetingHistoryRepository meetingHistoryRepository;

  /**
   * 같이볼까요 신청기록 남기는 메서드
   *
   * @param member  신청한 Member
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

  /**
   * Member ID 에 해당하는 유저의 같이볼까요 생성 or 참여 기록 조회하는 메서드
   * @param memberId 조회하려는 사용자의 ID
   * @param pageable 페이징 처리를 위한 객체. Page , size
   */
  public List<WatchMeetingHistory> readMeetingHistory(String memberId, Pageable pageable) {
    return meetingHistoryRepository
        .findAllByMemberMemberIdWithFetch(memberId, pageable);
  }
}
