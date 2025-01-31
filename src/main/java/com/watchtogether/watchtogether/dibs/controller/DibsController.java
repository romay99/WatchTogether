package com.watchtogether.watchtogether.dibs.controller;

import com.watchtogether.watchtogether.dibs.dto.DibsResponseDto;
import com.watchtogether.watchtogether.dibs.service.DibsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dibs")
@Tag(name = "찜하기", description = "영화 \"찜하기\" 관련 기능입니다.")
public class DibsController {
  private final DibsService dibsService;

  @PostMapping()
  @PreAuthorize("hasRole('ROLE_USER')")
  @Operation(summary = "찜 하기",description = "영화를 \"찜 목록\" 에 추가합니다. "
      + "상영 가능 상태인 영화는 추가할 수 없습니다.")
  public ResponseEntity<DibsResponseDto> dibsOnAMovie(@RequestParam Long movieId) {
    // SecurityContextHolder 에서 인증된 사용자의 ID 를 가져온다.
    String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
    return ResponseEntity.ok(dibsService.divsOnAMovie(movieId, memberId));
  }
}
