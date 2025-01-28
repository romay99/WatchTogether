package com.watchtogether.watchtogether.cinema.controller;

import com.watchtogether.watchtogether.cinema.dto.CinemaDto;
import com.watchtogether.watchtogether.cinema.service.CinemaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.watchtogether.watchtogether.cinema.entity.Cinema;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cinema")
public class CinemaController {

  private final CinemaService cinemaService;

  /**
   * 극장을 등록하는 메서드
   *
   * @param dto 입력받은 극장의 정보를 담은 DTO
   */
  @PreAuthorize("hasRole('ROLE_PARTNER')") // 파트너 계정만 접근가능
  @PostMapping("/cinema")
  public ResponseEntity<Cinema> registerCinema(@RequestBody CinemaDto dto) {
    // SecurityContextHolder 에서 인증된 사용자의 ID 를 가져온다.
    String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
    Cinema cinema = cinemaService.registerCinema(dto, memberId);
    return ResponseEntity.ok().body(cinema);
  }

  /**
   * 극장 정보 삭제하는 메서드
   */
  @PreAuthorize("hasRole('ROLE_PARTNER')") // 파트너 계정만 접근가능
  @DeleteMapping("/cinema")
  public ResponseEntity<?> deleteCinema() {
    // SecurityContextHolder 에서 인증된 사용자의 ID 를 가져온다.
    String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
    cinemaService.deleteCinema(memberId);
    return ResponseEntity.ok().body("극장 정보 삭제가 완료되었습니다.");
  }

  /**
   * 극장 정보 수정하는 메서드
   *
   * @param dto 극장의 수정할 정보를 담은 DTO
   */
  @PreAuthorize("hasRole('ROLE_PARTNER')") // 파트너 계정만 접근가능
  @PutMapping("/cinema")
  public ResponseEntity<Cinema> modifyCinema(@RequestBody CinemaDto dto) {
    // SecurityContextHolder 에서 인증된 사용자의 ID 를 가져온다.
    String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
    Cinema cinema = cinemaService.modifyCinema(dto, memberId);
    return ResponseEntity.ok().body(cinema);
  }
}
