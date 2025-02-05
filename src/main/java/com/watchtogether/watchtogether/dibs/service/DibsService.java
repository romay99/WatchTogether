package com.watchtogether.watchtogether.dibs.service;

import com.watchtogether.watchtogether.dibs.dto.DibsResponseDto;
import com.watchtogether.watchtogether.dibs.entity.Dibs;
import com.watchtogether.watchtogether.dibs.repository.DibsRepository;
import com.watchtogether.watchtogether.exception.custom.AlreadyDibsException;
import com.watchtogether.watchtogether.exception.custom.MemberNotFoundException;
import com.watchtogether.watchtogether.exception.custom.MovieAlreadyScreenAbleException;
import com.watchtogether.watchtogether.member.entity.Member;
import com.watchtogether.watchtogether.member.repository.MemberRepository;
import com.watchtogether.watchtogether.movie.dto.MovieIdNameDateDto;
import com.watchtogether.watchtogether.movie.dto.MovieListPageDto;
import com.watchtogether.watchtogether.movie.entity.Movie;
import jakarta.persistence.Tuple;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DibsService {

  private final MemberRepository memberRepository;
  private final DibsRepository dibsRepository;

  public DibsResponseDto divsOnAMovie(Long movieId, String memberId) {
    // Member 존재 검증, Movie 존재 검증, 찜 내역 검증 3가지를 하나의 쿼리로 조회한다.
    Tuple result = dibsRepository.findMovieAndMemberAndCheckDibs(
            movieId, memberId)
        .orElseThrow(() -> new MemberNotFoundException("영화 또는 사용자 정보가 존재하지 않습니다."));

    Member member = (Member) result.get(0);  // 조회된 Member
    Movie movie = (Movie) result.get(1);      // 조회된 Movie
    boolean existsDibs = (boolean) result.get(2); // 기존 찜 존재 여부

    if (member == null || movie == null) {
      throw new MemberNotFoundException("영화 또는 사용자 정보가 존재하지 않습니다.");
    }

    log.info("ID = {}, Title = {}, T/F = {} ", member.getMemberId(), movie.getTitle(), existsDibs);

    // 이미 상영가능한 영화를 찜하려 할때 예외 발생
    if (movie.isScreenAble()) {
      throw new MovieAlreadyScreenAbleException("해당 영화는 이미 상영 가능한 영화입니다.");
    }

    // 이미 찜한 영화를 찜하려 할때 예외발생
    if (existsDibs) {
      throw new AlreadyDibsException("이미 찜한 영화입니다.");
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
   * @return DELETE 문으로 삭제된 데이터 수를 return
   */
  @Transactional
  public int deleteMemberDibs(String memberId, Long movieId) {
    // 사용자 정보 존재하지 않으면 예외 발생
    Member member = memberRepository.findByMemberId(memberId)
        .orElseThrow(() -> new MemberNotFoundException("사용자가 존재하지 않습니다"));

    // DELETE 문으로 영향 받은 row 수를 return
    return dibsRepository.deleteDibs(memberId, movieId);
  }
}
