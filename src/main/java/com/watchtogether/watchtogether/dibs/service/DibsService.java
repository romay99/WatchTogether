package com.watchtogether.watchtogether.dibs.service;

import com.watchtogether.watchtogether.dibs.dto.DibsResponseDto;
import com.watchtogether.watchtogether.dibs.entity.Dibs;
import com.watchtogether.watchtogether.dibs.repository.DibsRepository;
import com.watchtogether.watchtogether.exception.custom.MemberNotFoundException;
import com.watchtogether.watchtogether.exception.custom.MovieAlreadyScreenAbleException;
import com.watchtogether.watchtogether.exception.custom.MovieDataNotFoundException;
import com.watchtogether.watchtogether.member.entity.Member;
import com.watchtogether.watchtogether.member.repository.MemberRepository;
import com.watchtogether.watchtogether.movie.entity.Movie;
import com.watchtogether.watchtogether.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DibsService {

  private final MovieRepository movieRepository;
  private final MemberRepository memberRepository;
  private final DibsRepository dibsRepository;

  public DibsResponseDto divsOnAMovie(Long movieId, String memberId) {
    // 사용자 정보 존재하지 않으면 예외 발생
    Member member = memberRepository.findByMemberId(memberId)
        .orElseThrow(() -> new MemberNotFoundException("사용자가 존재하지 않습니다"));

    // 영화 정보 존재하지 않으면 예외 발생
    Movie movie = movieRepository.findById(movieId).orElseThrow(
        () -> new MovieDataNotFoundException("영화정보가 존재하지 않습니다.")
    );

    // 상영 가능한 영화를 찜 목록에 추가하려할 때 예외 발생
    if (movie.isScreenAble()) {
      throw new MovieAlreadyScreenAbleException("해당 영화는 이미 상영 가능한 영화입니다.");
    }

    // Dibs Repository 에 저장
    Dibs savedDibs = dibsRepository.save(Dibs.builder()
        .movie(movie)
        .member(member)
        .build());

    log.info("{}님의 찜목록에 {} 가 추가 되었습니다.", memberId, movie.getTitle());
    return DibsResponseDto.builder()
        .movieTitle(savedDibs.getMovie().getTitle())
        .username(savedDibs.getMember().getName())
        .dateTime(savedDibs.getDateTime().toString())
        .build();
  }
}
