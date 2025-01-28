package com.watchtogether.watchtogether.cinema.service;

import com.watchtogether.watchtogether.cinema.dto.CinemaDto;
import com.watchtogether.watchtogether.cinema.entity.Cinema;
import com.watchtogether.watchtogether.cinema.repository.CinemaRepository;
import com.watchtogether.watchtogether.exception.custom.CinemaNotFoundException;
import com.watchtogether.watchtogether.exception.custom.MemberNotFoundException;
import com.watchtogether.watchtogether.exception.custom.PartnerCanHaveOneCinemaException;
import com.watchtogether.watchtogether.member.entity.Member;
import com.watchtogether.watchtogether.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.stereotype.Service;
import org.locationtech.jts.geom.Point;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CinemaService {

  private final CinemaRepository cinemaRepository;
  private final GeometryFactory geometryFactory;
  private final MemberRepository memberRepository;


  /**
   * 극장 등록하는 메서드
   *
   * @param dto      등록하려는 극장의 정보가 담긴 DTO
   * @param memberId 극장을 등록하려는 사용자의 ID
   * @return 등록된 극장의 정보
   */
  @Transactional
  public Cinema registerCinema(CinemaDto dto, String memberId) {

    // 이 사용자의 계정으로 등록된 극장이 이미 존재한다면 예외 발생
    if (cinemaRepository.existsByMemberMemberId(memberId)) {
      throw new PartnerCanHaveOneCinemaException("하나의 파트너 계정에는 하나의 극장만 등록 가능합니다.");
    }

    // 입력받은 위도,경도를 이용해 Point 데이터를 생성
    Point point = geometryFactory.createPoint(
        new Coordinate(dto.getLongitude(), dto.getLatitude()));

    // 메서드를 호출한 사용자의 ID 로 사용자 데이터를 가져온다.
    Member member = memberRepository.findByMemberId(memberId)
        .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 사용자입니다."));

    // 극장 데이터를 저장
    Cinema cinema = cinemaRepository.save(Cinema.builder().name(dto.getName())
        .description(dto.getDescription())
        .coordinates(point)
        .member(member)
        .build());

    log.info("{} 극장이 등록 완료되었습니다.", dto.getName());
    return cinema;
  }

  /**
   * 극장 데이터 삭제하는 메서드
   *
   * @param memberId 삭제하려는 사용자의 ID
   */
  public void deleteCinema(String memberId) {
    cinemaRepository.deleteByMemberMemberId(memberId);
    log.info("{} 님의 계정에 연동된 극장 정보가 삭제되었습니다.", memberId);
  }

  /**
   * 극장 데이터 수정하는 메서드
   *
   * @param dto      수정할 극장 정보를 담은 DTO
   * @param memberId 수정할 극장에 연동된 사용자의 ID
   */
  public Cinema modifyCinema(CinemaDto dto, String memberId) {
    // 극장 정보가 존재하지 않으면 예외 발생
    Cinema cinema = cinemaRepository.findByMemberMemberId(memberId).orElseThrow(() ->
        new CinemaNotFoundException("극장 정보가 존재하지 않습니다."));

    if (dto.getName() != null) {
      cinema.setName(dto.getName());
    }
    if (dto.getDescription() != null) {
      cinema.setName(dto.getDescription());
    }
    // 위치 정보 수정
    if (dto.getLongitude() != null || dto.getLatitude() != null) {
      Double longitude = dto.getLongitude();
      Double latitude = dto.getLatitude();

      if (longitude == null) {
        longitude = cinema.getCoordinates().getX();
      }
      if (latitude == null) {
        latitude = cinema.getCoordinates().getY();
      }

      // 새로운 Point 변수 생성
      Point point = geometryFactory.createPoint(
          new Coordinate(longitude, latitude));

      cinema.setCoordinates(point);
    }
    // 수정된 entity 저장
    log.info("{} 님의 계정에 연동된 극장의 정보가 수정되었습니다.", memberId);
    return cinemaRepository.save(cinema);
  }
}
