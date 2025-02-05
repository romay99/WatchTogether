package com.watchtogether.watchtogether.meeting.service;

import com.watchtogether.watchtogether.api.TmdbApiService;
import com.watchtogether.watchtogether.cinema.entity.Cinema;
import com.watchtogether.watchtogether.cinema.repository.CinemaRepository;
import com.watchtogether.watchtogether.exception.custom.CinemaNotFoundException;
import com.watchtogether.watchtogether.exception.custom.MeetingAlreadyExistException;
import com.watchtogether.watchtogether.exception.custom.MemberNotEnoughPointException;
import com.watchtogether.watchtogether.exception.custom.MemberNotFoundException;
import com.watchtogether.watchtogether.exception.custom.MovieNotScreenAbleException;
import com.watchtogether.watchtogether.history.meeting.service.MeetingHistoryService;
import com.watchtogether.watchtogether.history.point.entity.TransactionDetail;
import com.watchtogether.watchtogether.history.point.service.TransactionHistoryService;
import com.watchtogether.watchtogether.meeting.dto.MeetingCreateDto;
import com.watchtogether.watchtogether.meeting.entity.WatchMeeting;
import com.watchtogether.watchtogether.meeting.repository.MeetingRepository;
import com.watchtogether.watchtogether.member.entity.Member;
import com.watchtogether.watchtogether.member.repository.MemberRepository;
import com.watchtogether.watchtogether.movie.entity.Movie;
import com.watchtogether.watchtogether.movie.repository.MovieRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MeetingService {

  private final MeetingRepository meetingRepository;
  private final MemberRepository memberRepository;
  private final MovieRepository movieRepository;
  private final CinemaRepository cinemaRepository;
  private final TmdbApiService tmdbApiService;
  private final TransactionHistoryService transactionHistoryService;
  private final MeetingHistoryService meetingHistoryService;

  @Transactional
  public WatchMeeting createWatchMeeting(String memberId, MeetingCreateDto dto) {
    // 존재하지 않는 사용자라면 예외 밟생
    Member member = memberRepository.findByMemberId(memberId)
        .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 사용자입니다."));

    Long cinemaCode = dto.getCinemaId(); // 극장 정보
    Long movieCode = dto.getMovieId(); // 영화 정보
    LocalDateTime time = dto.getTime(); // 시간 정보

    // 영화데이터가 존재하는지 검증
    // 존재하지 않는다면 TMDB API 호출 후 저장
    Movie movie = movieRepository.findById(movieCode)
        .orElseGet(() ->
            {
              Movie newMovie = tmdbApiService.findMovieById(movieCode);
              return movieRepository.save(newMovie);
            }
        );

    // 극장데이터가 존재하는지 검증
    Cinema cinema = cinemaRepository.findById(cinemaCode)
        .orElseThrow(() -> new CinemaNotFoundException("존재하지 않는 극장입니다."));

    // 상영 불가능한 영화로 같이볼까요 생성할때 예외처리
    if (!movie.isScreenAble()) {
      throw new MovieNotScreenAbleException("상영 가능한 작품이 아닙니다.");
    }

    // 사용자의 포인트가 충분하지 못할때 예외처리
    if (member.getPoint() < 15000) {
      throw new MemberNotEnoughPointException("포인트가 부족합니다. 결제후 진행해 주세요");
    }

    // 해당시간 이미 같은 영화가 같은 극장에 상영 예정이면 예외 발생
    if (meetingRepository.existsCheck(cinemaCode, movieCode, time)) {
      throw new MeetingAlreadyExistException("해당 시간 이미 같이볼래요 가 존재합니다.");
    }
    // 포인트 사용기록 저장
    transactionHistoryService.usePoint(member, 15000, TransactionDetail.WATCH_MEETING);
    // 같이볼까요 생성 -> 저장
    WatchMeeting watchMeeting = WatchMeeting.builder()
        .maxPeople(cinema.getMaxPeople())
        .movie(movie)
        .cinema(cinema)
        .member(member)
        .nowPeople(1)
        .build();
    WatchMeeting savedMeeting = meetingRepository.save(watchMeeting);
    // 같이볼까요 신청기록 저장
    meetingHistoryService.recordMeetingHistory(member,savedMeeting);

    return watchMeeting;
  }
}
