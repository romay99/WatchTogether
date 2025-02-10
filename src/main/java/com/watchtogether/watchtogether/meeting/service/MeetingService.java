package com.watchtogether.watchtogether.meeting.service;

import com.watchtogether.watchtogether.api.service.TmdbApiService;
import com.watchtogether.watchtogether.cinema.entity.Cinema;
import com.watchtogether.watchtogether.cinema.repository.CinemaRepository;
import com.watchtogether.watchtogether.exception.custom.CinemaNotFoundException;
import com.watchtogether.watchtogether.exception.custom.MeetingAlreadyExistException;
import com.watchtogether.watchtogether.exception.custom.MeetingAlreadyRegisterException;
import com.watchtogether.watchtogether.exception.custom.MeetingMaxPeopleException;
import com.watchtogether.watchtogether.exception.custom.MeetingNotFoundException;
import com.watchtogether.watchtogether.exception.custom.MemberNotEnoughPointException;
import com.watchtogether.watchtogether.exception.custom.MemberNotFoundException;
import com.watchtogether.watchtogether.exception.custom.MovieNotScreenAbleException;
import com.watchtogether.watchtogether.history.meeting.dto.MeetingHistoryDto;
import com.watchtogether.watchtogether.history.meeting.entity.WatchMeetingHistory;
import com.watchtogether.watchtogether.history.meeting.repository.MeetingHistoryRepository;
import com.watchtogether.watchtogether.history.meeting.service.MeetingHistoryService;
import com.watchtogether.watchtogether.history.point.entity.TransactionDetail;
import com.watchtogether.watchtogether.history.point.service.TransactionHistoryService;
import com.watchtogether.watchtogether.meeting.dto.MeetingCreateDto;
import com.watchtogether.watchtogether.meeting.dto.MeetingJoinResponseDto;
import com.watchtogether.watchtogether.meeting.entity.WatchMeeting;
import com.watchtogether.watchtogether.meeting.repository.MeetingRepository;
import com.watchtogether.watchtogether.member.entity.Member;
import com.watchtogether.watchtogether.member.repository.MemberRepository;
import com.watchtogether.watchtogether.movie.entity.Movie;
import com.watchtogether.watchtogether.movie.repository.MovieRepository;
import com.watchtogether.watchtogether.movie.service.MovieService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
  private final MeetingHistoryRepository meetingHistoryRepository;
  private final MovieService movieService;

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
              return movieService.saveMovie(newMovie);
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
    transactionHistoryService.usePoint(member, -15000, TransactionDetail.WATCH_MEETING);
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
    meetingHistoryService.recordMeetingHistory(member, savedMeeting);

    log.info("{} 님이 {} 영화의 같이볼까요를 생성했습니다. 생성일시 = {}",
        memberId, movie.getTitle(), savedMeeting.getDateTime());

    return watchMeeting;
  }

  @Transactional
  public MeetingJoinResponseDto joinMeeting(Long meetingId, String memberId) {
    // 존재하지 않는 사용자라면 예외 발생
    Member member = memberRepository.findByMemberId(memberId)
        .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 사용자입니다."));

    // point 가 충분하지 않을 시 예외 발생
    if (member.getPoint() < 15000) {
      throw new MemberNotEnoughPointException("포인트 충전후 이용 가능합니다.");
    }

    // 같이볼까요가 존재하지 않는다면 예외 발생
    WatchMeeting watchMeeting = meetingRepository.findById(meetingId)
        .orElseThrow(() -> new MeetingNotFoundException("존재하지 않는 같이볼까요 입니다."));

    // 인원이 모두 찼으면 예외 발생
    if (watchMeeting.getMaxPeople() == watchMeeting.getNowPeople() + 1) {
      throw new MeetingMaxPeopleException("정원초과");
    }

    if (meetingHistoryRepository.existsByMemberMemberIdAndMeetingCode(memberId, meetingId)) {
      throw new MeetingAlreadyRegisterException("'같이볼까요' 중복 신청은 불가능합니다.");
    }

    // 같이볼까요 신청기록 저장
    WatchMeetingHistory watchMeetingHistory = meetingHistoryService.recordMeetingHistory(member,
        watchMeeting);
    // 포인트 사용기록 저장
    transactionHistoryService.usePoint(member, -15000, TransactionDetail.WATCH_MEETING);

    // '같이볼까요' 현재 인원 업데이트
    watchMeeting.setNowPeople(watchMeeting.getNowPeople() + 1);
    WatchMeeting newMeeting = meetingRepository.save(watchMeeting);

    log.info("{} 님이 {} 영화의 {} 시간에 같이볼까요를 참여했습니다.",
        memberId, watchMeeting.getMovie().getTitle(), newMeeting.getDateTime());

    // DTO 로 변환후 return
    return MeetingJoinResponseDto.builder()
        .meetingDateTime(newMeeting.getDateTime())
        .joinDateTime(watchMeetingHistory.getJoinDateTime())
        .message("성공적으로 같이볼까요 신청이 완료되었습니다.")
        .cinemaName(newMeeting.getCinema().getName())
        .build();
  }

  public List<MeetingHistoryDto> meetinghistory(int page, int size, String memberId) {
    // 존재하지 않는 사용자라면 예외 밟생
    Member member = memberRepository.findByMemberId(memberId)
        .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 사용자입니다."));

    Pageable pageable = PageRequest.of(--page, size); // 페이징을 위한 Pageable 객체 생성

    List<WatchMeetingHistory> list = meetingHistoryService // WatchMeetingHistory -> DTO 변환 필요
        .readMeetingHistory(memberId, pageable);

    // MeetingHistoryDto List 로 변환 후 return
    return list.stream().map(data -> MeetingHistoryDto.builder()
        .canceledDateTime(data.getCancelDateTime())
        .dateTime(data.getJoinDateTime())
        .movieTitle(data.getMeeting().getMovie().getTitle())
        .cinemaName(data.getMeeting().getCinema().getName())
        .build()).toList();
  }
}
