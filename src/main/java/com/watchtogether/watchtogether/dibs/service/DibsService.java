package com.watchtogether.watchtogether.dibs.service;

import com.watchtogether.watchtogether.dibs.dto.DibsResponseDto;
import com.watchtogether.watchtogether.dibs.entity.Dibs;
import com.watchtogether.watchtogether.dibs.repository.DibsRepository;
import com.watchtogether.watchtogether.exception.custom.MemberNotFoundException;
import com.watchtogether.watchtogether.exception.custom.MovieAlreadyScreenAbleException;
import com.watchtogether.watchtogether.exception.custom.MovieDataNotFoundException;
import com.watchtogether.watchtogether.member.entity.Member;
import com.watchtogether.watchtogether.member.repository.MemberRepository;
import com.watchtogether.watchtogether.movie.dto.MovieIdNameDateDto;
import com.watchtogether.watchtogether.movie.dto.MovieListPageDto;
import com.watchtogether.watchtogether.movie.entity.Movie;
import com.watchtogether.watchtogether.movie.repository.MovieRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

  /**
   * member ID 를 이용해 찜한 영화 목록을 불러온다.
   *
   * @param memberId 사용자 ID
   * @param page     페이지 번호
   * @param size     페이지 사이즈
   * @return 데이터갯수, 페이지 번호 , List 를 포함한 DTO
   */
  public MovieListPageDto getDibsMovieList(String memberId, int page, int size) {
    // 사용자 정보 존재하지 않으면 예외 발생
    Member member = memberRepository.findByMemberId(memberId)
        .orElseThrow(() -> new MemberNotFoundException("사용자가 존재하지 않습니다"));

    // 입력받은 페이징 정보 설정
    Pageable pageable = PageRequest.of(--page, size);
    List<MovieIdNameDateDto> list = new ArrayList<>();

    Page<Dibs> dibsList = dibsRepository.findAllByMemberMemberId(memberId, pageable);
    dibsList.stream().forEach(dibs -> list.add(MovieIdNameDateDto.toDto(dibs.getMovie())));

    return MovieListPageDto.builder()
        .totalElements(dibsList.getTotalElements())
        .totalPageNumber(dibsList.getNumber())
        .data(list)
        .build();
  }

  /**
   * 찜 삭제하는 메서드
   *
   * @param memberId memberId
   * @param movieId  찜 목록에서 삭제할 영화 PK
   * @return false = 삭제 실패,찜 데이터 존재하지 않음 / true = 삭제 성공
   */
  public boolean deleteMemberDibs(String memberId, Long movieId) {
    // 사용자 정보 존재하지 않으면 예외 발생
    Member member = memberRepository.findByMemberId(memberId)
        .orElseThrow(() -> new MemberNotFoundException("사용자가 존재하지 않습니다"));

    Optional<Dibs> dibs = dibsRepository.findByMemberIdAndMovieCode(memberId, movieId);

    if (dibs.isEmpty()) {
      return false;
    } else {
      Dibs dib = dibs.get();
      log.info("{} 영화가 정상적으로 찜목록에서 삭제되었습니다.", dib.getMovie().getTitle());
      dibsRepository.delete(dibs.get());
      return true;
    }
  }
}
