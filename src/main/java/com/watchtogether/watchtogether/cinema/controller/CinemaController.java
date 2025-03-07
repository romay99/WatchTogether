package com.watchtogether.watchtogether.cinema.controller;

import com.watchtogether.watchtogether.cinema.dto.CinemaDto;
import com.watchtogether.watchtogether.cinema.service.CinemaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cinema")
@Tag(name = "극장",description = "극장 관련 API 입니다.")
public class CinemaController {

  private final CinemaService cinemaService;

  /**
   * 극장을 등록하는 메서드
   *
   * @param dto 입력받은 극장의 정보를 담은 DTO
   */
  @PreAuthorize("hasRole('ROLE_PARTNER')") // 파트너 계정만 접근가능
  @PostMapping()
  @Operation(summary = "극장 등록",description = "파트너 사용자의 계정에 극장을 등록합니다.(최대 1개)")
  public ResponseEntity<CinemaDto> registerCinema(@RequestBody CinemaDto dto) {
    // SecurityContextHolder 에서 인증된 사용자의 ID 를 가져온다.
    String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
    CinemaDto cinema = cinemaService.registerCinema(dto, memberId);
    return ResponseEntity.ok().body(cinema);
  }

  /**
   * 극장 정보 삭제하는 메서드
   */
  @PreAuthorize("hasRole('ROLE_PARTNER')") // 파트너 계정만 접근가능
  @DeleteMapping()
  @Operation(summary = "극장 삭제",description = "파트너 계정에 연동되어있는 극장 정보를 삭제합니다.")
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
  @Operation(summary = "극장 수정",description = "파트너 계정에 연동되어 있는 극장의 정보를 수정합니다.")
  @PreAuthorize("hasRole('ROLE_PARTNER')") // 파트너 계정만 접근가능
  @PutMapping()
  public ResponseEntity<CinemaDto> modifyCinema(@RequestBody CinemaDto dto) {
    // SecurityContextHolder 에서 인증된 사용자의 ID 를 가져온다.
    String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
    CinemaDto cinema = cinemaService.modifyCinema(dto, memberId);
    return ResponseEntity.ok().body(cinema);
  }

  /**
   * 극장 정보를 조회하는 메서드
   * @param cinemaId 조회할 극장의 ID
   * @return 극장의 정보를 담은 DTO
   */
  @Operation(summary = "극장 조회",description = "극장의 ID 값으로 극장 정보를 조회합니다.")
  @GetMapping("/info")
  public ResponseEntity<CinemaDto> getCinema(@RequestParam Long cinemaId) {
    return ResponseEntity.ok(cinemaService.getCinemaInfoById(cinemaId));
  }
}
